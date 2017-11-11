package dao;

import entities.Categories;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;

public class CategoriesDAOImplTest {

    private CategoriesDAOImpl objectUnderTest;

    @Before
    public void setUp() {
        objectUnderTest = new CategoriesDAOImpl();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        objectUnderTest.setSessionFactory(sessionFactory);
    }

    @After
    public void tearDown() {
        //do nothing
    }

    private static final int EXPECTED_CATEGORY_ID = 8;
    private static final String EXPECTED_CATEGORY_NAME = "Seafood";
    private static final String EXPECTED_CATEGORY_DESCRIPTION = "Seaweed and fish";

    private static final String NEW_CATEGORY_NAME = "New Category";
    private static final String NEW_CATEGORY_DESCRIPTION = "New Description";

    private static final Categories EXPECTED_CATEGORY;
    private static final Categories NEW_CATEGORY;

    static {
        EXPECTED_CATEGORY = new Categories(EXPECTED_CATEGORY_NAME, EXPECTED_CATEGORY_DESCRIPTION);
        NEW_CATEGORY = new Categories(NEW_CATEGORY_NAME, NEW_CATEGORY_DESCRIPTION);
    }

    @Test
    public void addCategory() throws Exception {
        int currentCategoriesQuantity = objectUnderTest.listCategories().size();
        objectUnderTest.addCategory(NEW_CATEGORY);
        Assert.assertEquals(currentCategoriesQuantity + 1, objectUnderTest.listCategories().size());
        Assert.assertEquals(currentCategoriesQuantity + 1, objectUnderTest.getCategoryById(currentCategoriesQuantity + 1).getCategoryId());
        Assert.assertEquals(NEW_CATEGORY.getCategoryName(), objectUnderTest.getCategoryById(currentCategoriesQuantity + 1).getCategoryName());
        Assert.assertEquals(NEW_CATEGORY.getDescription(), objectUnderTest.getCategoryById(currentCategoriesQuantity + 1).getDescription());
    }

    @Test
    public void updateCategory() throws Exception {
        Categories category = objectUnderTest.getCategoryById(EXPECTED_CATEGORY_ID);
        Assert.assertEquals(EXPECTED_CATEGORY.getCategoryName(), category.getCategoryName());
        Assert.assertEquals(EXPECTED_CATEGORY.getDescription(), category.getDescription());

        category.setCategoryName(NEW_CATEGORY_NAME);
        category.setDescription(NEW_CATEGORY_DESCRIPTION);
        objectUnderTest.updateCategory(category);

        Assert.assertEquals(NEW_CATEGORY_NAME, objectUnderTest.getCategoryById(EXPECTED_CATEGORY_ID).getCategoryName());
        Assert.assertEquals(NEW_CATEGORY_DESCRIPTION, objectUnderTest.getCategoryById(EXPECTED_CATEGORY_ID).getDescription());

        category.setCategoryName(EXPECTED_CATEGORY_NAME);
        category.setDescription(EXPECTED_CATEGORY_DESCRIPTION);
        objectUnderTest.updateCategory(category);
    }

    @Ignore
    @Test
    public void listCategories() throws Exception {

    }

    @Test
    public void getCategoryById() throws Exception {
        Categories category = objectUnderTest.getCategoryById(EXPECTED_CATEGORY_ID);
        Assert.assertEquals(EXPECTED_CATEGORY.getCategoryName(), category.getCategoryName());
        Assert.assertEquals(EXPECTED_CATEGORY.getDescription(), category.getDescription());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void removeCategoryById() throws Exception {
        int currentCategoriesQuantity = objectUnderTest.listCategories().size();
        objectUnderTest.addCategory(NEW_CATEGORY);
        objectUnderTest.removeCategoryById(currentCategoriesQuantity + 1);
        objectUnderTest.getCategoryById(currentCategoriesQuantity + 1);
    }

}