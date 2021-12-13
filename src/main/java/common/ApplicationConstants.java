package common;

import com.google.common.collect.Lists;
import model.bbo.model.Process;
import model.bbo.model.*;
import model.bbo.model.Thing;
import model.stamp.model.*;

import java.util.List;

public class ApplicationConstants {

    //TODO to config

    // / or #
    public static final String ONTOLOGY_IRI_SUFFIX = "/";

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
            TimeExpression.class
    );

    public static final List<Class<? extends model.stamp.model.Thing>> STAMP_CLASSES = Lists.newArrayList(
            ControlledProcess.class,
            Controller.class,
            Capability.class,
            StructureComponent.class,
            Structure.class
    );

}
