package com.itechart.sample.security;

import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.security.service.SecuredDao;
import com.itechart.sample.security.util.RoleBuilder;
import com.itechart.sample.security.util.UserBuilder;
import com.itechart.sample.security.util.auth.WithUser;
import com.itechart.sample.service.DictionaryService;
import com.itechart.sample.service.RoleService;
import com.itechart.sample.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static com.itechart.sample.security.util.junit.ThrowableAssert.assertThrown;
import static org.mockito.Mockito.when;

/**
 * Unit for testing security annotations that can be applied to Hibernate DAO
 *
 * @author andrei.samarou
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:securityTestContext.xml")
@Transactional
public class HibernateDaoAnnotationsTest {

    @Autowired
    private UserService userServiceMock;
    @Autowired
    private RoleService roleServiceMock;
    @Autowired
    private DictionaryService dictionaryServiceMock;

    @Autowired
    private SecuredDao securedDao;

    @Test
    public void testBadDeclaration() {
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration1);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration2);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration3);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration4);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration5);
        assertThrown(IllegalStateException.class, () -> securedDao.doBadDeclaration6(null));
    }

    @Test
    @WithUser("userRoleUser")
    public void testGoodDeclaration() {
        securedDao.doGoodDeclaration1();
        securedDao.doGoodDeclaration2();
        securedDao.doGoodDeclaration3();
        securedDao.doGoodDeclaration4();
        securedDao.doGoodDeclaration5();
        securedDao.doGoodDeclaration6();
        securedDao.doGoodDeclaration7();
        securedDao.doGoodDeclaration8();
        securedDao.doGoodDeclaration9();
        securedDao.doGoodDeclaration10();
    }

    @PostConstruct
    public void initialize() {
        Role userRole = RoleBuilder.create("User").privilege("TestObject", "READ").build();
        Role managerRole = RoleBuilder.create("Manager", userRole).privilege("TestObject", "WRITE").build();
        mock(userRole, managerRole);

        mock(UserBuilder.create("userRoleUser").role(userRole).build());
        mock(UserBuilder.create("userRoleManager").role(managerRole).build());

        ObjectType objectType = new ObjectType();
        objectType.setId(1L);
        objectType.setName("TestObject");
        when(dictionaryServiceMock.getObjectTypeByName("TestObject")).thenReturn(objectType);
    }

    private void mock(User user) {
        when(userServiceMock.findByName(user.getName())).thenReturn(user);
    }

    private void mock(Role... role) {
        when(roleServiceMock.getRoles()).thenReturn(Arrays.asList(role));
    }
}
