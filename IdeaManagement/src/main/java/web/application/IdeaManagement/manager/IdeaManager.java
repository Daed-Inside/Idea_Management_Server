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
import web.application.IdeaManagement.model.response.*;
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
    UserRepository userRepository;
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

            //-----------add system view---------------//
            IdeaViewCount newCount = new IdeaViewCount();
            newCount.setIdeaId(createdIdea.getId());
            newCount.setLatestViewDate(new Date());
            newCount.setCreatedUser("SYSTEM");
            newCount.setCreatedDate(new Date());
            //------------Add reaction count-------//
            IdeaReaction newReaction = new IdeaReaction();

            newReaction.setCreatedDate(new Date());
            newReaction.setCreatedUser("SYSTEM");
            newReaction.setIdeaId(createdIdea.getId());
            newReaction.setEvaluation(0);
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
            ideaReactionRepo.save(newReaction);
            ideaViewCountRepo.save(newCount);
            return createdIdea.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1l;
        }
    }

    public Long updateFile(List<IdeaAttachmentRequest> files, Long id, String email) {
        try {
            List<IdeaAttachment> listSave = new ArrayList<>();
            if (files != null) {
                for (IdeaAttachmentRequest file : files) {
                    IdeaAttachment ida = new IdeaAttachment();
                    ida.setIdeaId(id);
                    ida.setDownloadUrl(file.getDownloadUrl());
                    ida.setFileName(file.getFileName());
                    ida.setFileType(file.getFileType());
                    ida.setPath(file.getFilePath());
                    ida.setCreatedDate(new Date());
                    ida.setCreatedUser(email);
                    listSave.add(ida);
                }
            }
            ideaAttachmentRepo.saveAll(listSave);
            return 1l;
        } catch (Exception e) {
            return 0l;
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

    public IdeaDetailResponse getIdeaDetail(Long id, String userId) {
        try {
            User currUser = userRepository.findById(userId).get();
            List<Role> roles = new ArrayList<>(currUser.getRoles());
            Boolean checkAdmin = false;
            if (!roles.isEmpty() && roles.get(0).getId() == 1l) {
                checkAdmin = true;
            }
            IdeaDetailResponse specIdea = ideaRepository.getIdeaDetail(id);
            List<IdeaAttachment> listAttach = ideaAttachmentRepo.findByIdeaId(id);
            List<FileResponse> fileResponses = listAttach.stream().map(x -> {
                FileResponse newFileRes = new FileResponse();
                newFileRes.setOriginal(x.getDownloadUrl());
                newFileRes.setThumbnail(x.getDownloadUrl());
                newFileRes.setDownloadUrl(x.getDownloadUrl());
                newFileRes.setFileName(x.getFileName());
                newFileRes.setFileType(x.getFileType());
                return newFileRes;
            }).collect(Collectors.toList());
            specIdea.setListAttachment(fileResponses);
            if (checkAdmin == false) {
                if (specIdea.getIsAnonymous() == true) {
                    specIdea.setCreatedUser(null);
                }
            }
            return specIdea;
        } catch (Exception e) {
            return new IdeaDetailResponse();
        }
    }

    public List<ExcelExportResponse> getExportData(String year, String semester, Long department, Long topic) {
        try {
            return ideaRepository.getExcelData(year, semester, department, topic);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public DashboardResponse getDashboard(String year, String semester, Long department, Long topic) {
        try {
            DashboardResponse response = ideaRepository.getDashboard(year, semester, department, topic);
            Long ideaNoCmt = ideaSpecification.ideaWithNoComment(year, semester, department, topic);
            Long anonymousCmt = ideaSpecification.anonymousComment(year, semester, department, topic);
            response.setAnonymousComment(anonymousCmt);
            response.setIdeaNoComment(ideaNoCmt);

            //-------------------Bar chart------------------------//
            List<String> listDept = new ArrayList<>();
            List<Long> listIdea = new ArrayList<>();
            List<Long> listComment = new ArrayList<>();
            List<Object[]> bar1 = ideaRepository.FirstBarChart(year, semester, department, topic);
            for (Object[] obj : bar1) {
                listDept.add(obj[0].toString());
                listIdea.add(Long.parseLong(obj[1].toString()));
                listComment.add(Long.parseLong(obj[2].toString()));
            }
            FirstBarChartResponse newbar1 = new FirstBarChartResponse(listDept, listIdea, listComment);
            //--------------------Pie Chart-----------------------//
            List<String> listDeptPie = new ArrayList<>();
            List<Double> listPerc = new ArrayList<>();
            List<Object[]> pie = ideaRepository.FirstBarChart(year, semester, department, topic);
            for (Object[] obj : pie) {
                Long ideaCount = Long.parseLong(obj[1].toString());
                Double percentage = ((double) Math.round(((ideaCount*1.0) /response.getTotalIdea()) * 100) / 100) * 100;
                listDeptPie.add(obj[0].toString());
                listPerc.add(percentage);
            }
            PieChartResponse newPie = new PieChartResponse(listDeptPie, listPerc);
            //--------------------Second bar chart------------------//
            List<String> listDept2 = new ArrayList<>();
            List<Long> listUser = new ArrayList<>();
            List<Object[]> bar2 = userRepository.countUserByDept();
            for (Object[] obj : bar2) {
                listDept2.add(obj[0].toString());
                listUser.add(Long.parseLong(obj[1].toString()));
            }
            SecondBarChartResponse newBar2 = new SecondBarChartResponse(listDept2, listUser);
            response.setFirstBarChart(newbar1);
            response.setPieChart(newPie);
            response.setSecondBarChar(newBar2);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
