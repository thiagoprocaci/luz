package br.com.tbp.model;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class CoreEntityTest {
    private CoreEntity coreEntity1;
    private CoreEntity coreEntity2;
    private CoreEntity coreEntity3;
    private CoreEntity coreEntity4;
    private CoreEntity coreEntity5;
    private CoreEntity coreEntity6;

    @Before
    public void doBefore() {
        coreEntity1 = buildCoreEntityInstance("1");
        coreEntity2 = buildCoreEntityInstance("1");
        coreEntity3 = buildCoreEntityInstance("3");
        coreEntity4 = buildCoreEntityInstance(null);
        coreEntity5 = coreEntity4;
        coreEntity6 = buildCoreEntityInstance(null);
    }

    @Test
    public void testCoreEntity() {
        assertNotNull(coreEntity1.hashCode());
        assertNotNull(coreEntity2.hashCode());
        assertNotNull(coreEntity3.hashCode());
        assertNotNull(coreEntity4.hashCode());
        assertNotNull(coreEntity5.hashCode());
        assertNotNull(coreEntity6.hashCode());
        assertTrue(coreEntity1.equals(coreEntity2));
        assertTrue(coreEntity1.hashCode() == coreEntity2.hashCode());
        assertTrue(coreEntity4.equals(coreEntity5));
        assertTrue(coreEntity4.hashCode() == coreEntity5.hashCode());
        assertFalse(coreEntity5.equals(null));
        assertFalse(coreEntity5.equals(""));
        assertFalse("".equals(coreEntity5));
        assertFalse("".hashCode() == coreEntity5.hashCode());
        assertFalse(coreEntity1.equals(coreEntity3));
        assertFalse(coreEntity1.hashCode() == coreEntity3.hashCode());
        assertFalse(coreEntity5.equals(coreEntity6));
        assertTrue(coreEntity5.hashCode() == coreEntity6.hashCode());
        assertFalse(coreEntity6.equals(coreEntity1));
        assertFalse(coreEntity6.hashCode() == coreEntity1.hashCode());

    }

    private CoreEntity buildCoreEntityInstance(String id) {
        CoreEntity coreEntity = new CoreEntity();
        coreEntity.setId(id);
        return coreEntity;
    }

}
