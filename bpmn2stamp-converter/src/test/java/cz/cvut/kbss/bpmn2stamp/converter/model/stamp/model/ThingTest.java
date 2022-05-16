package cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model;

import cz.cvut.kbss.jopa.model.BeanListenerAspect;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ThingTest {

	@Test
	public void testIsManageable() {
		Thing thing = new Thing();
		Assertions.assertThat(thing).isInstanceOf(BeanListenerAspect.Manageable.class).withFailMessage(
				() -> "class " + thing.getClass() + " should implement " + BeanListenerAspect.Manageable.class + "class. " +
						"Probably aspectj:compile was not performed for STAMP model classes."
		);
	}

}