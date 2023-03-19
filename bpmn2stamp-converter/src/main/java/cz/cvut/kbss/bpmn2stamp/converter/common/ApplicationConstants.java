package cz.cvut.kbss.bpmn2stamp.converter.common;

import com.google.common.collect.Lists;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.CallActivity;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.NormalSequenceFlow;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.TimeExpression;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.TimerEventDefinition;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.UserTask;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Process;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.EndEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.InterruptingBoundaryEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.StartEvent;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Capability;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.ControlledProcess;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Controller;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Structure;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.StructureComponent;

import java.util.List;

public class ApplicationConstants {

    //TODO to config

    // / or #
    public static final String ONTOLOGY_IRI_SUFFIX = "/";

    /**
     * Extension of config files (taken from .bos archive), containing information about actor mapping. Extension should
     * NOT start with dot.
     */
    public static final String CONF_FILE_EXTENSION = "conf";

    /**
     * Role id suffix to distinguish from Group with the same name.
     */
    public static final String ORG_ROLE_SUFFIX = "_role";

    /**
     * Group id suffix to distinguish from Role with the same name.
     */
    public static final String ORG_GROUP_SUFFIX = "_group";

    /**
     * Composite id delimiter.
     */
    public static final String COMPOSITE_ID_DELIMITER = "-";

    public static final List<Class<? extends Thing>> BBO_CLASSES = Lists.newArrayList(
            StartEvent.class,
            UserTask.class, 
            NormalSequenceFlow.class,
            Process.class,
            Role.class,
            Group.class,
            InterruptingBoundaryEvent.class,
            EndEvent.class,
            TimerEventDefinition.class,
            TimeExpression.class,
            CallActivity.class
    );

    public static final List<Class<? extends cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing>> STAMP_CLASSES = Lists.newArrayList(
            ControlledProcess.class,
            Controller.class,
            Capability.class,
            StructureComponent.class,
            Structure.class
    );

}
