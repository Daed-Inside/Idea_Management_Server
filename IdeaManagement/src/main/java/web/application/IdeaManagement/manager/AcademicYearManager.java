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
import web.application.IdeaManagement.entity.AcademicYear;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.entity.IdeaAttachment;
import web.application.IdeaManagement.entity.Topic;
import web.application.IdeaManagement.model.request.AcademicYearRequestModel;
import web.application.IdeaManagement.model.request.IdeaRequestModel;
import web.application.IdeaManagement.model.response.AcademicYearResponseModel;
import web.application.IdeaManagement.model.response.IdeaResponseModel;
import web.application.IdeaManagement.repository.AcademicYearRepository;
import web.application.IdeaManagement.repository.IdeaAttachmentRepository;
import web.application.IdeaManagement.repository.IdeaRepository;
import web.application.IdeaManagement.repository.TopicRepository;
import web.application.IdeaManagement.specification.AcademicYearSpecification;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AcademicYearManager {
    @Autowired
    AcademicYearRepository academicYearRepository;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AcademicYearSpecification academicYearSpecification;


    public Integer createAcademicYear(String semester, String year, Date startDate, Date endDate) {
        try{
            AcademicYear newAcademicYear = new AcademicYear();
            newAcademicYear.setSemester(semester);
            newAcademicYear.setYear(year);
            newAcademicYear.setStartDate(startDate);
            newAcademicYear.setEndDate(endDate);
            newAcademicYear.setCreatedDate(new Date());

            academicYearRepository.save(newAcademicYear);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


    public Boolean updateAcademicYear(AcademicYearRequestModel req) {
        try{
            AcademicYear currentAcademicYear = academicYearRepository.findById(req.getId()).get();
            currentAcademicYear.setSemester(req.getSemester() != null ? req.getSemester() : currentAcademicYear.getSemester());
            currentAcademicYear.setYear(req.getYear() != null ? req.getYear() : currentAcademicYear.getYear());
            currentAcademicYear.setStartDate(req.getStartDate() != null ? req.getStartDate() : currentAcademicYear.getStartDate());
            currentAcademicYear.setEndDate(req.getEndDate() != null ? req.getEndDate() : currentAcademicYear.getEndDate());
            currentAcademicYear.setModifiedDate(new Date());
            currentAcademicYear.setModifiedUser((req.getUserId()));
            academicYearRepository.save(currentAcademicYear);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<AcademicYearResponseModel> getAcademicYearWithModel(String searchKey) {
        try{
            List<AcademicYearResponseModel> listResult = academicYearSpecification.getAcademicYearWithModel(searchKey);
            return listResult;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PageDto getAcademicYearWithSpec(String searchKey) {
        try {
            Sort sort = responseUtils.getSort("id", "DESC");
            Page<AcademicYear> listResult = academicYearRepository.findAll(academicYearSpecification.filterAcademicYear(searchKey), PageRequest.of(0, 10, sort));
            return PageDto.builder()
                    .content(listResult.getContent())
                    .numberOfElements(listResult.getNumberOfElements())
                    .page(listResult.getNumber())
                    .size(listResult.getSize())
                    .totalPages(listResult.getTotalPages())
                    .totalElements(listResult.getTotalElements())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
