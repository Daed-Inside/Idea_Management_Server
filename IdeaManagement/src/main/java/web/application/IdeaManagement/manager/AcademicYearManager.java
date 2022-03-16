package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.*;
import web.application.IdeaManagement.model.request.AcademicYearRequest;
import web.application.IdeaManagement.model.response.AcademicYearReponse;
import web.application.IdeaManagement.repository.AcademicYearRepository;
import web.application.IdeaManagement.specification.AcademicYearSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

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


    public Integer createAcademicYear(AcademicYearRequest req) {
        try{
            AcademicYear newAcademicYear = modelMapper.map(req, AcademicYear.class);
            newAcademicYear.setCreatedDate(new Date());
            academicYearRepository.save(newAcademicYear);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public PageDto getAcademicYear(String searchKey, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Sort sort = responseUtils.getSort(sortBy, sortType);
            Integer pageNum = page - 1;
            Page<AcademicYear> pageTopic = academicYearRepository.findAll(academicYearSpecification.filterAcademicYear(searchKey), PageRequest.of(pageNum, limit, sort));
            return PageDto.builder()
                    .content(pageTopic.getContent())
                    .numberOfElements(pageTopic.getNumberOfElements())
                    .page(page)
                    .size(pageTopic.getSize())
                    .totalPages(pageTopic.getTotalPages())
                    .totalElements(pageTopic.getTotalElements())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    public List<AcademicYearReponse> getSemesterByYear(String year) {
        try{
            List<AcademicYearReponse> listResult = academicYearSpecification.getSemeserByYear(year);
            return listResult;
        }catch (Exception e) {
            return null;
        }
    }

    public List<AcademicYear> getSemesterByYearTemp(String year) {
        try{
            List<AcademicYear> listResult = academicYearRepository.findAllByYear(year);
            return listResult;
        }catch (Exception e) {
            return null;
        }
    }
}
