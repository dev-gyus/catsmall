package catsmall.cat.search;

import catsmall.cat.entity.item.Item;
import catsmall.cat.entity.item.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static catsmall.cat.entity.item.QItem.item;

@Component
public class SearchRepository {
    private EntityManager em;
    private JPAQueryFactory queryFactory;
    private ModelMapper modelMapper;

    public SearchRepository(EntityManager em, ModelMapper modelMapper) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.modelMapper = modelMapper;
    }

    public Page<SearchDto> findAllItemPagingByKeyword(String keyword, Pageable pageable) {
        QueryResults<Item> result = queryFactory
                .selectFrom(item)
                .where(item.name.contains(keyword).or(item.content.contains(keyword)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();
        List<SearchDto> collect = result.getResults().stream().map(i -> {
            SearchDto searchDto = modelMapper.map(i, SearchDto.class);
            searchDto.setItemId(i.getId());
            searchDto.setItemName(i.getName());
            return searchDto;
        }).collect(Collectors.toList());

        return new PageImpl<>(collect, pageable, result.getTotal());
    }
}
