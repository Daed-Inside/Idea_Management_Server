package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.entity.IdeaAttachment;
import web.application.IdeaManagement.entity.Topic;
import web.application.IdeaManagement.model.request.IdeaRequestModel;
import web.application.IdeaManagement.model.response.IdeaResponseModel;
import web.application.IdeaManagement.repository.IdeaAttachmentRepository;
import web.application.IdeaManagement.repository.IdeaRepository;
import web.application.IdeaManagement.repository.TopicRepository;
import web.application.IdeaManagement.specification.IdeaSpecification;
import web.application.IdeaManagement.utils.LanguageUtils;
import web.application.IdeaManagement.utils.OSUtils;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class IdeaManager {
    @Autowired
    IdeaRepository ideaRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    IdeaAttachmentRepository ideaAttachmentRepo;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IdeaSpecification ideaSpecification;

    public Integer createIdea(Long categoryId, Long topicId, String ideaTile,
                              String ideaContent, Boolean isAnonymous,
                              MultipartFile[] files, String username, String userId) {
        try {
//            Topic topic = topicRepository.findById(topicId).get();
//            if (topic.getEndDate().before(new Date())) {
//                return -2;
//            }
            Idea newIdea = new Idea();
            newIdea.setUserId(userId);
            newIdea.setIsAnonymous(isAnonymous);
            newIdea.setTopicId(topicId);
            newIdea.setCategoryId(categoryId);
            newIdea.setIdeaContent(ideaContent);
            newIdea.setIdeaTitle(ideaTile);
            newIdea.setCreatedDate(new Date());
            newIdea.setCreatedUser(username);
            Idea createdIdea =  ideaRepository.save(newIdea);

            List<IdeaAttachment> listAttachment = new ArrayList<>();
            DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
            String strDate = dateFormat.format(new Date());
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    IdeaAttachment ida = new IdeaAttachment();
                    MultipartFile file = files[i];
                    String fileName = file.getOriginalFilename();
                    String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
                    String basename = LanguageUtils.replaceSpecialChar(tokens[0]);
                    String extension = tokens[1];
                    String saveFileName = basename + "_" + strDate + "." + extension;

                    File dirFile = new File(OSUtils.getApplicationDataFolder()
                            + File.separator + "idea");
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                    Path path = Paths.get(dirFile + File.separator + saveFileName);

                    try {
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl("http://fc4e-1-55-198-254.ngrok.io")
                            .path("/file/download/")
                            .path("idea" + "/")
                            .path(saveFileName)
                            .toUriString();
                    ida.setIdeaId(createdIdea.getId());
                    ida.setUserId(userId);
                    ida.setDownloadUrl(fileDownloadUri);
                    ida.setFileName(fileName);
                    ida.setPath(path.toAbsolutePath().toString());
                    ida.setCreatedDate(new Date());
                    ida.setCreatedUser(username);
                    listAttachment.add(ida);
                }
            }
            ideaAttachmentRepo.saveAll(listAttachment);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Boolean updateIdea(IdeaRequestModel req) {
        try {
            Idea newIdea = ideaRepository.findById(req.getId()).get();
            newIdea.setIdeaContent(req.getIdeaContent() != null ? req.getIdeaContent() : newIdea.getIdeaContent());
            newIdea.setIdeaTitle(req.getIdeaTitle() != null ? req.getIdeaTitle() : newIdea.getIdeaTitle());
            newIdea.setModifiedDate(new Date());
            newIdea.setModifiedUser(req.getUserId());
            ideaRepository.save(newIdea);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteIdea(Long id) {
        try {
            ideaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<IdeaResponseModel> getIdeaWithModel(String searchKey) {
        try {
            List<IdeaResponseModel> listResult = ideaSpecification.getIdeaWithModel(searchKey);
            return listResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PageDto getIdeaWithSpec(String searchKey, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Integer pageNum = page -1;
            Sort sort = responseUtils.getSort(sortBy, sortType);
            Page<Idea> listResult = ideaRepository.findAll(ideaSpecification.filterIdea(searchKey), PageRequest.of(pageNum, limit, sort));
            return PageDto.builder()
                    .content(listResult.getContent())
                    .numberOfElements(listResult.getNumberOfElements())
                    .page(page)
                    .size(listResult.getSize())
                    .totalPages(listResult.getTotalPages())
                    .totalElements(listResult.getTotalElements())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getIdeaDetail(Long id) {
        try {
            Idea specIdea = ideaRepository.findById(id).get();
            List<IdeaAttachment> listAttach = ideaAttachmentRepo.findByIdeaId(id);
        } catch (Exception e) {

        }
    }
}
