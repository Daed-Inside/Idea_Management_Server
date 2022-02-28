package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.entity.*;
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


    public List<AcademicYear> getAcademicYearWithSpec(String searchKey) {
        try{
            List<AcademicYear> listResult = academicYearRepository.findAll(academicYearSpecification.filterAcademic(searchKey));
            return listResult;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
