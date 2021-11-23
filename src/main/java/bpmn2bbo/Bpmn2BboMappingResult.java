package bpmn2bbo;

import model.bbo.model.FlowElement;
import model.bbo.model.Process;
import model.bbo.model.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bpmn2BboMappingResult {

    private Map<String, Role> roles;
    private Map<String, Process> processes;
    private Map<String, FlowElement> flowElements;

    public Bpmn2BboMappingResult() {
        roles = new HashMap<>();
        processes = new HashMap<>();
        flowElements = new HashMap<>();
    }

    public Map<String, Role> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Role> roles) {
        this.roles = roles;
    }

    public Map<String, Process> getProcesses() {
        return processes;
    }

    public void setProcesses(Map<String, Process> processes) {
        this.processes = processes;
    }

    public Map<String, FlowElement> getFlowElements() {
        return flowElements;
    }

    public void setFlowElements(Map<String, FlowElement> flowElements) {
        this.flowElements = flowElements;
    }
}
