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
import web.application.IdeaManagement.entity.*;
import web.application.IdeaManagement.model.request.IdeaAttachmentRequest;
import web.application.IdeaManagement.model.request.IdeaRequest;
import web.application.IdeaManagement.model.request.IdeaRequestModel;
import web.application.IdeaManagement.model.response.CategoryReponse;
import web.application.IdeaManagement.model.response.FileResponse;
import web.application.IdeaManagement.model.response.IdeaDetailResponse;
import web.application.IdeaManagement.model.response.IdeaResponseModel;
import web.application.IdeaManagement.repository.*;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class IdeaManager {
    @Autowired
    IdeaRepository ideaRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    IdeaViewCountRepository ideaViewCountRepo;
    @Autowired
    IdeaReactionRepository ideaReactionRepo;
    @Autowired
    IdeaAttachmentRepository ideaAttachmentRepo;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IdeaSpecification ideaSpecification;

    public Long createIdea(IdeaRequest ideaRequest, String username, String userId) {
        try {
//            Topic topic = topicRepository.findById(topicId).get();
//            if (topic.getEndDate().before(new Date())) {
//                return -2;
//            }
            Idea newIdea = new Idea();
            newIdea.setUserId(userId);
            newIdea.setIsAnonymous(ideaRequest.getContributor());
            newIdea.setTopicId(ideaRequest.getTopicId());
            newIdea.setCategoryId(ideaRequest.getCategoryId());
            newIdea.setIdeaContent(ideaRequest.getDescription());
            newIdea.setIdeaTitle(ideaRequest.getTitle());
            newIdea.setCreatedDate(new Date());
            newIdea.setCreatedUser(username);
            Idea createdIdea = ideaRepository.save(newIdea);

            List<IdeaAttachment> listAttachment = new ArrayList<>();
            if (ideaRequest.getFiles() != null) {
                for (IdeaAttachmentRequest attach : ideaRequest.getFiles()) {
                    IdeaAttachment ida = new IdeaAttachment();
                    ida.setIdeaId(createdIdea.getId());
                    ida.setUserId(userId);
                    ida.setDownloadUrl(attach.getDownloadUrl());
                    ida.setFileName(attach.getFileName());
                    ida.setPath(attach.getFilePath());
                    ida.setCreatedDate(new Date());
                    ida.setCreatedUser(username);
                    listAttachment.add(ida);
                }
            }
            //-----------add system view---------------//
            IdeaViewCount newCount = new IdeaViewCount();
            newCount.setIdeaId(createdIdea.getId());
            newCount.setLatestViewDate(new Date());
            newCount.setCreatedUser("SYSTEM");
            newCount.setCreatedDate(new Date());
            //------------Add reaction count-------//
            IdeaReaction likeReaction = new IdeaReaction();
            IdeaReaction dislikeReaction = new IdeaReaction();

            likeReaction.setCreatedDate(new Date());
            likeReaction.setCreatedUser("SYSTEM");
            likeReaction.setIdeaId(createdIdea.getId());
            likeReaction.setEvaluation(1);

            dislikeReaction.setCreatedDate(new Date());
            dislikeReaction.setCreatedUser("SYSTEM");
            dislikeReaction.setIdeaId(createdIdea.getId());
            dislikeReaction.setEvaluation(-1);
            //---------------------------------------//
//            DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
//            String strDate = dateFormat.format(new Date());
//            if (files != null && files.length > 0) {
//                for (int i = 0; i < files.length; i++) {
//                    IdeaAttachment ida = new IdeaAttachment();
//                    MultipartFile file = files[i];
//                    String fileName = file.getOriginalFilename();
//                    String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
//                    String basename = LanguageUtils.replaceSpecialChar(tokens[0]);
//                    String extension = tokens[1];
//                    String saveFileName = basename + "_" + strDate + "." + extension;
//
//                    File dirFile = new File(OSUtils.getApplicationDataFolder()
//                            + File.separator + "idea");
//                    if (!dirFile.exists()) {
//                        dirFile.mkdirs();
//                    }
//                    Path path = Paths.get(dirFile + File.separator + saveFileName);
//
//                    try {
//                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    String fileDownloadUri = ServletUriComponentsBuilder.fromHttpUrl("http://fc4e-1-55-198-254.ngrok.io")
//                            .path("/file/download/")
//                            .path("idea" + "/")
//                            .path(saveFileName)
//                            .toUriString();
//                    if (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg")) {
//                        ida.setPicture(1);
//                    } else {
//                        ida.setPicture(0);
//                    }
//                    ida.setIdeaId(createdIdea.getId());
//                    ida.setUserId(userId);
//                    ida.setDownloadUrl(fileDownloadUri);
//                    ida.setFileName(fileName);
//                    ida.setPath(path.toAbsolutePath().toString());
//                    ida.setCreatedDate(new Date());
//                    ida.setCreatedUser(username);
//                    listAttachment.add(ida);
//                }
//            }
            ideaReactionRepo.save(likeReaction);
            ideaReactionRepo.save(dislikeReaction);
            ideaViewCountRepo.save(newCount);
            ideaAttachmentRepo.saveAll(listAttachment);
            return createdIdea.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1l;
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

//    public List<IdeaResponseModel> getIdeaWithModel(String searchKey) {
//        try {
//            List<IdeaResponseModel> listResult = ideaSpecification.getIdeaWithModel(searchKey);
//            return listResult;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public PageDto getIdeaWithSpec(String searchKey, Long acadId, Long deptId, Long topicId, Long categoryId, Integer page, Integer limit, Integer sort) {
        try {
            Integer pageNum = page - 1;
//            Sort sort = responseUtils.getSort(sortBy, sortType);
            Map<String, Object> mapResult = ideaSpecification.getIdeaWithModel(searchKey, acadId, deptId, topicId, categoryId, sort, page, limit);
            List<CategoryReponse> listRes = (List<CategoryReponse>) mapResult.get("data");
            Long totalItems = mapResult.get("count") != null ? (Long) mapResult.get("count") : 0l;
            Integer totalPage = responseUtils.getPageCount(totalItems, limit);
            return PageDto.builder()
                    .content(listRes)
                    .numberOfElements(Math.toIntExact(totalItems))
                    .page(page)
                    .size(limit)
                    .totalPages(totalPage)
                    .totalElements(totalItems)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public IdeaDetailResponse getIdeaDetail(Long id) {
        try {
            IdeaDetailResponse specIdea = ideaRepository.getIdeaDetail(id);
            List<IdeaAttachment> listAttach = ideaAttachmentRepo.findByIdeaId(id);
            List<FileResponse> fileResponses = listAttach.stream().map(x -> {
                FileResponse newFileRes = new FileResponse();
                newFileRes.setOriginal(x.getPath());
                newFileRes.setThumbnail(x.getPath());
                newFileRes.setDownloadUrl(x.getDownloadUrl());
                newFileRes.setFileName(x.getFileName());
                return newFileRes;
            }).collect(Collectors.toList());
            specIdea.setListAttachment(fileResponses);
            return specIdea;
        } catch (Exception e) {
            return new IdeaDetailResponse();
        }
    }
}
