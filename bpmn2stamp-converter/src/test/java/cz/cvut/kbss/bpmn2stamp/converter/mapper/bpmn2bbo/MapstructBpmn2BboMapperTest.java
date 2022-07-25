package cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.TimeExpression;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.TimerEventDefinition;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TExpression;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TTimerEventDefinition;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class MapstructBpmn2BboMapperTest {
	
	private MapstructBpmn2BboMapper mapper = Mappers.getMapper(MapstructBpmn2BboMapper.class);
	
	private static final String POOL_NAME = "poolName";
	private static final String POOL_ID = "poolId";
	
	@Before
	public void useContext() {
		MapstructBpmn2BboMapper.MappingContext mappingContext = new MapstructBpmn2BboMapper.MappingContext(POOL_ID, POOL_NAME);
		mapper.setMappingContext(mappingContext);
	}

	@Test
	public void testTimerEventDefinitionToTimerEventDefinition() {
		TTimerEventDefinition timerEventDefinition = new TTimerEventDefinition();
		timerEventDefinition.setId("eventdef-Within 7 days");
		String attachedProcessId = "abcd12345";

		TimerEventDefinition actual = mapper.timerEventDefinitionToTimerEventDefinition(timerEventDefinition, attachedProcessId);
		processAfterMappings();

		TimerEventDefinition expected = new TimerEventDefinition();
		expected.setId("http://bpmn2stamp.org/default/ontology/poolid_abcd12345_eventdef-within_7_days");
		
		Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	public void testTimeExpressionToTimeExpression() {
		TExpression tExpression = new TExpression();
		tExpression.setId("Within 7 days");
		tExpression.getContent().add("604800000L");
		String attachedProcessId = "abcd12345";

		TimeExpression actual = mapper.timeExpressionToTimeExpression(tExpression, attachedProcessId);
		processAfterMappings();

		TimeExpression expected = new TimeExpression();
		expected.setId("http://bpmn2stamp.org/default/ontology/poolid_abcd12345_604800000");
		expected.setProperties(Map.of(
				Vocabulary.s_p_value, Set.of("PT168H")
		));
		
		Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}
	
	// should be called after all mappings within the process
	public void processAfterMappings() {
		mapper.getAfterMapping().forEach(Runnable::run);
	}
}