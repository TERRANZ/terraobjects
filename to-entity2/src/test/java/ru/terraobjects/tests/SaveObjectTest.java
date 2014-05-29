package ru.terraobjects.tests;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.LoggerFactory;
import ru.terraobjects.manager.ObjectsManager;

import java.util.Date;

/**
 * Date: 29.05.14
 * Time: 17:26
 */
public class SaveObjectTest extends TestCase {
    public SaveObjectTest() {
        super("SaveObjectTest Tests");
        BasicConfigurator.configure();
    }

    public void test1() throws Exception {
        TestObject testObject = new TestObject();
        testObject.setName("test object name");
        testObject.setCreationDate(new Date());
        testObject.setSize(12345d);
        testObject.setSum(7890l);
        LoggerFactory.getLogger(this.getClass()).info("Created: " + testObject.toString());
        ObjectsManager<TestObject> objectsManager = new ObjectsManager<>();
        objectsManager.saveOrUpdate(testObject);
        TestObject testObject2 = objectsManager.load(TestObject.class, testObject.getId());
        Assert.assertNotNull(testObject2);
        LoggerFactory.getLogger(this.getClass()).info("Loaded: " + testObject2.toString());
    }
}
