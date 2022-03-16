package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.AcademicYear;
import web.application.IdeaManagement.entity.Department;
import web.application.IdeaManagement.model.request.AcademicYearRequest;
import web.application.IdeaManagement.model.request.DepartmentRequest;
import web.application.IdeaManagement.repository.AcademicYearRepository;
import web.application.IdeaManagement.repository.DepartmentRepository;
import web.application.IdeaManagement.specification.AcademicYearSpecification;
import web.application.IdeaManagement.specification.DepartmentSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.Date;

@Service
@Transactional
public class DepartmentManager {
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    DepartmentSpecification departmentSpecification;


    public Integer createDepartment(DepartmentRequest reqBody) {
        try{
            Department newDepartment = modelMapper.map(reqBody, Department.class);
            newDepartment.setCreatedDate(new Date());
            departmentRepository.save(newDepartment);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public PageDto getDepartment(String searchKey, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Sort sort = responseUtils.getSort(sortBy, sortType);
            Integer pageNum = page - 1;
            Page<Department> pageDepartment = departmentRepository.findAll(departmentSpecification.filterDepartment(searchKey), PageRequest.of(pageNum, limit, sort));
            return PageDto.builder()
                    .content(pageDepartment.getContent())
                    .numberOfElements(pageDepartment.getNumberOfElements())
                    .page(page)
                    .size(pageDepartment.getSize())
                    .totalPages(pageDepartment.getTotalPages())
                    .totalElements(pageDepartment.getTotalElements())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
