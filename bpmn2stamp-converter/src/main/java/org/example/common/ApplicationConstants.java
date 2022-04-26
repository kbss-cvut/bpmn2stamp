package org.example.common;

import com.google.common.collect.Lists;
import org.example.model.bbo.model.Process;
import org.example.model.bbo.model.Thing;
import org.example.model.bbo.model.EndEvent;
import org.example.model.bbo.model.Group;
import org.example.model.bbo.model.InterruptingBoundaryEvent;
import org.example.model.bbo.model.NormalSequenceFlow;
import org.example.model.bbo.model.Role;
import org.example.model.bbo.model.StartEvent;
import org.example.model.bbo.model.TimeExpression;
import org.example.model.bbo.model.TimerEventDefinition;
import org.example.model.bbo.model.UserTask;
import org.example.model.stamp.model.Capability;
import org.example.model.stamp.model.ControlledProcess;
import org.example.model.stamp.model.Controller;
import org.example.model.stamp.model.Structure;
import org.example.model.stamp.model.StructureComponent;

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

    public static final List<Class<? extends org.example.model.stamp.model.Thing>> STAMP_CLASSES = Lists.newArrayList(
            ControlledProcess.class,
            Controller.class,
            Capability.class,
            StructureComponent.class,
            Structure.class
    );

}
