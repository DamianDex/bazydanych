package dao;

import entities.Products;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;

public class ProductsDAOImplTest {
    private ProductsDAOImpl objectUnderTest;


    private static final int EXPECTED_PRODUCT_ID = 77;
    private static final String EXPECTED_PRODUCT_NAME = "Original Frankfurter grüne Soße";
    private static final String EXPECTED_PRODUCT_QUANTITY_PER_UNIT = "12 boxes";
    private static final double EXPECTED_PRODUCT_UNIT_PRICE = 13.0;
    private static final int EXPECTED_PRODUCT_UNITS_IN_STOCK = 32;
    private static final int EXPECTED_PRODUCT_UNITS_ON_ORDER = 0;
    private static final int EXPECTED_PRODUCT_REORDER_LEVEL = 15;
    private static final int EXPECTED_PRODUCT_DISCONTINUED = 0;

    private static final String NEW_PRODUCT_NAME = "New Product";
    private static final String NEW_PRODUCT_QUANTITY_PER_UNIT = "12 bottle";
    private static final double NEW_PRODUCT_UNIT_PRICE = 1;
    private static final int NEW_PRODUCT_UNITS_IN_STOCK = 1;
    private static final int NEW_PRODUCT_UNITS_ON_ORDER = 1;
    private static final int NEW_PRODUCT_REORDER_LEVEL = 1;
    private static final int NEW_PRODUCT_DISCONTINUED = 1;

    private static final Products EXPECTED_PRODUCT;
    private static final Products NEW_PRODUCT;

    static {
        EXPECTED_PRODUCT = new Products(EXPECTED_PRODUCT_NAME,
                EXPECTED_PRODUCT_QUANTITY_PER_UNIT,
                EXPECTED_PRODUCT_UNIT_PRICE, EXPECTED_PRODUCT_UNITS_IN_STOCK,
                EXPECTED_PRODUCT_UNITS_ON_ORDER, EXPECTED_PRODUCT_REORDER_LEVEL,
                EXPECTED_PRODUCT_DISCONTINUED);
        NEW_PRODUCT = new Products(NEW_PRODUCT_NAME,
                NEW_PRODUCT_QUANTITY_PER_UNIT,
                NEW_PRODUCT_UNIT_PRICE, NEW_PRODUCT_UNITS_IN_STOCK,
                NEW_PRODUCT_UNITS_ON_ORDER, NEW_PRODUCT_REORDER_LEVEL,
                NEW_PRODUCT_DISCONTINUED);
    }

    @Before
    public void setUp() {
        objectUnderTest = new ProductsDAOImpl();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        objectUnderTest.setSessionFactory(sessionFactory);
    }

    @After
    public void tearDown() {
        //do nothing
    }


    @Test
    public void addProduct() throws Exception {
        int currentProductsQuantity = objectUnderTest.listProducts().size();
        objectUnderTest.addProduct(NEW_PRODUCT);
        Assert.assertEquals(currentProductsQuantity + 1, objectUnderTest.listProducts().size());
        Assert.assertEquals(currentProductsQuantity + 1, objectUnderTest.getProductById(currentProductsQuantity + 1).getProductid());
        Assert.assertEquals(NEW_PRODUCT.getProductname(), objectUnderTest.getProductById(currentProductsQuantity + 1).getProductname());
        Assert.assertEquals(NEW_PRODUCT.getUnitprice(), objectUnderTest.getProductById(currentProductsQuantity + 1).getUnitprice(), 10);
        Assert.assertEquals(NEW_PRODUCT.getDiscontinued(), objectUnderTest.getProductById(currentProductsQuantity + 1).getDiscontinued());
        Assert.assertEquals(NEW_PRODUCT.getReorderlevel(), objectUnderTest.getProductById(currentProductsQuantity + 1).getReorderlevel());
        Assert.assertEquals(NEW_PRODUCT.getUnitsinstock(), objectUnderTest.getProductById(currentProductsQuantity + 1).getUnitsinstock());
        Assert.assertEquals(NEW_PRODUCT.getUnitsonorder(), objectUnderTest.getProductById(currentProductsQuantity + 1).getUnitsonorder());
    }

    @Test
    public void updateProduct() throws Exception {
        Products products = objectUnderTest.getProductById(EXPECTED_PRODUCT_ID);
        Assert.assertEquals(EXPECTED_PRODUCT.getProductname(), products.getProductname());
        Assert.assertEquals(EXPECTED_PRODUCT.getUnitprice(), products.getUnitprice(), 10);
        Assert.assertEquals(EXPECTED_PRODUCT.getDiscontinued(), products.getDiscontinued());
        Assert.assertEquals(EXPECTED_PRODUCT.getReorderlevel(), products.getReorderlevel());
        Assert.assertEquals(EXPECTED_PRODUCT.getUnitsinstock(), products.getUnitsinstock());
        Assert.assertEquals(EXPECTED_PRODUCT.getUnitsonorder(), products.getUnitsonorder());

        products.setProductname(NEW_PRODUCT_NAME);
        products.setUnitprice(NEW_PRODUCT_UNIT_PRICE);
        products.setDiscontinued(NEW_PRODUCT_DISCONTINUED);
        products.setReorderlevel(NEW_PRODUCT_REORDER_LEVEL);
        products.setUnitsinstock(NEW_PRODUCT_UNITS_IN_STOCK);
        products.setUnitsonorder(NEW_PRODUCT_UNITS_ON_ORDER);
        objectUnderTest.updateProduct(products);

        Assert.assertEquals(NEW_PRODUCT_NAME, objectUnderTest.getProductById(EXPECTED_PRODUCT_ID).getProductname());
        Assert.assertEquals(NEW_PRODUCT_UNIT_PRICE, objectUnderTest.getProductById(EXPECTED_PRODUCT_ID).getUnitprice(), 10);
        Assert.assertEquals(NEW_PRODUCT_DISCONTINUED, objectUnderTest.getProductById(EXPECTED_PRODUCT_ID).getDiscontinued());
        Assert.assertEquals(NEW_PRODUCT_REORDER_LEVEL, objectUnderTest.getProductById(EXPECTED_PRODUCT_ID).getReorderlevel());
        Assert.assertEquals(NEW_PRODUCT_UNITS_IN_STOCK, objectUnderTest.getProductById(EXPECTED_PRODUCT_ID).getUnitsinstock());
        Assert.assertEquals(NEW_PRODUCT_UNITS_ON_ORDER, objectUnderTest.getProductById(EXPECTED_PRODUCT_ID).getUnitsonorder());

        products.setProductname(EXPECTED_PRODUCT_NAME);
        products.setUnitprice(EXPECTED_PRODUCT_UNIT_PRICE);
        products.setDiscontinued(EXPECTED_PRODUCT_DISCONTINUED);
        products.setReorderlevel(EXPECTED_PRODUCT_REORDER_LEVEL);
        products.setUnitsinstock(EXPECTED_PRODUCT_UNITS_IN_STOCK);
        products.setUnitsonorder(EXPECTED_PRODUCT_UNITS_ON_ORDER);
        objectUnderTest.updateProduct(products);
    }

    @Ignore
    @Test
    public void listProductsWithCategories() throws Exception {

    }

    @Ignore
    @Test
    public void listProducts() throws Exception {

    }

    @Test
    public void getProductById() throws Exception {
        Products products = objectUnderTest.getProductById(EXPECTED_PRODUCT_ID);
        Assert.assertEquals(EXPECTED_PRODUCT.getProductname(), products.getProductname());
        Assert.assertEquals(EXPECTED_PRODUCT.getUnitprice(), products.getUnitprice(), 10);
        Assert.assertEquals(EXPECTED_PRODUCT.getDiscontinued(), products.getDiscontinued());
        Assert.assertEquals(EXPECTED_PRODUCT.getReorderlevel(), products.getReorderlevel());
        Assert.assertEquals(EXPECTED_PRODUCT.getUnitsinstock(), products.getUnitsinstock());
        Assert.assertEquals(EXPECTED_PRODUCT.getUnitsonorder(), products.getUnitsonorder());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void removeProductById() throws Exception {
        int currentProductsQuantity = objectUnderTest.listProducts().size();
        objectUnderTest.addProduct(NEW_PRODUCT);
        objectUnderTest.removeProductById(currentProductsQuantity + 1);
        objectUnderTest.getProductById(currentProductsQuantity + 1);
    }
}