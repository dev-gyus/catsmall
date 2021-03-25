package catsmall.cat;

import catsmall.cat.admin.Roles;
import catsmall.cat.cart.Cart;
import catsmall.cat.entity.*;
import catsmall.cat.entity.item.CatFood;
import catsmall.cat.entity.item.CatToilet;
import catsmall.cat.entity.item.CatTower;
import catsmall.cat.member.Address;
import catsmall.cat.member.Member;
import catsmall.cat.repository.ItemCategoryRepository;
import catsmall.cat.service.CategoryService;
import catsmall.cat.service.ItemService;
import catsmall.cat.zzim.Zzim;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@Getter @Setter
@RequiredArgsConstructor
public class PrepareClass {
    private final Prepare prepare;

//    @PostConstruct
    public void init(){
        prepare.init();
    }

    @Component
//    @Transactional
    @RequiredArgsConstructor
    static class Prepare{
        private final EntityManager em;
        @Autowired
        private CategoryService categoryService;
        @Autowired
        private ItemService itemService;
        @Autowired
        private ItemCategoryRepository itemCategoryRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        public void init(){
            String password = "vmffkd495";
            Address address = new Address("12345", "신림로", "신림동 241-53", "4층 404호", null);
            Member member = new Member("cjsworbehd13@naver.com", passwordEncoder.encode(password), "규스", "규스스","01041638922", address, Roles.ROLE_USER);
            Member admin = new Member("cjsworbehd13@gmail.com", passwordEncoder.encode(password), "관리자", "admin","01000000000",address, Roles.ROLE_ADMIN);
            Zzim zzim = new Zzim(member);
            Zzim zzima = new Zzim(admin);
            Cart cart = new Cart(member);
            Cart carta = new Cart(admin);
            em.persist(member);
            em.persist(admin);

            Category catFood = new Category("CatFood");
            Category catToilet = new Category("CatToilet");
            Category catTower = new Category("CatTower");

            Category kitten = new Category("kitten");
            Category senior = new Category("senior");
            Category diet = new Category("diet");
            em.persist(kitten);
            em.persist(senior);
            em.persist(diet);
            Category small = new Category("소형");
            Category medium = new Category("중형");
            Category large = new Category("대형");
            em.persist(small);
            em.persist(medium);
            em.persist(large);
            Category secondF = new Category("2단");
            Category thirdF = new Category("3단");
            Category fourthF = new Category("4단");
            em.persist(secondF);
            em.persist(thirdF);
            em.persist(fourthF);

            catFood.addChild(kitten);
            catFood.addChild(senior);
            catFood.addChild(diet);

            catToilet.addChild(small);
            catToilet.addChild(medium);
            catToilet.addChild(large);

            catTower.addChild(secondF);
            catTower.addChild(thirdF);
            catTower.addChild(fourthF);
            em.persist(catFood);
            em.persist(catToilet);
            em.persist(catTower);
        }
    }
}
