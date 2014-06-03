package ru.terraobjects.tests;

import junit.framework.Assert;
import junit.framework.TestCase;
import ru.terraobjects.manager.ObjectsManager;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Date: 29.05.14
 * Time: 17:26
 */
public class SaveObjectTest extends TestCase {
    public SaveObjectTest() {
        super("SaveObjectTest Tests");
    }

    public void test1() throws Exception {
        TestObject testObject = new TestObject();
        testObject.setName("test object name");
        testObject.setCreationDate(new Date());
        testObject.setSize(12345d);
        testObject.setSum(7890l);
        ObjectsManager<TestObject> objectsManager = new ObjectsManager<>();
        objectsManager.saveOrUpdate(testObject);

        TestObject testObject2 = objectsManager.load(TestObject.class, testObject.getId());
        Assert.assertNotNull(testObject2);
    }

    public void test2() throws Exception {
        ObjectsManager<TestObject> objectsManager = new ObjectsManager<>();
        Logger.getAnonymousLogger().info(String.valueOf(objectsManager.getCount("test object name", "name")));
    }
}
