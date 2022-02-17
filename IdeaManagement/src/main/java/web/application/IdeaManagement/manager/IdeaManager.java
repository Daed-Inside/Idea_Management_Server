package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.Idea;
import web.application.IdeaManagement.model.request.IdeaRequestModel;
import web.application.IdeaManagement.model.response.IdeaResponseModel;
import web.application.IdeaManagement.repository.IdeaRepository;
import web.application.IdeaManagement.specification.IdeaSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class IdeaManager {
    @Autowired
    IdeaRepository ideaRepository;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    IdeaSpecification ideaSpecification;

    public Boolean createIdea(IdeaRequestModel req) {
        try {
            Idea newIdea = modelMapper.map(req, Idea.class);
            newIdea.setCreatedDate(new Date());
            newIdea.setCreatedUser(req.getUserId());
            ideaRepository.save(newIdea);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    public PageDto getIdeaWithSpec(String searchKey) {
        try {
            Sort sort = responseUtils.getSort("id", "DESC");
            Page<Idea> listResult = ideaRepository.findAll(ideaSpecification.filterIdea(searchKey), PageRequest.of(0, 10, sort));
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
