package cz.cvut.kbss.bpmn2stamp.converter.mapper.bbo2stamp;

import cz.cvut.kbss.bpmn2stamp.converter.mapper.AbstractMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.ControlledProcess;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bbo2StampMappingResult extends AbstractMappingResult<Thing> {

    private List<ControlledProcess> controlledProcesses;
    private List<Process> processes;

    public Bbo2StampMappingResult(Map<String, Thing> mappedObjectsById) {
        super(mappedObjectsById);
        this.controlledProcesses = new ArrayList<>();
        this.processes = new ArrayList<>();
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public List<ControlledProcess> getControlledProcesses() {
        return controlledProcesses;
    }

    public void setControlledProcesses(List<ControlledProcess> controlledProcesses) {
        this.controlledProcesses = controlledProcesses;
    }
}
