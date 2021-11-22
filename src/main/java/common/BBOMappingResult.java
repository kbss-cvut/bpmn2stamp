package common;

import model.bbo.model.FlowElement;
import model.bbo.model.Process;
import model.bbo.model.Role;

import java.util.ArrayList;
import java.util.List;

public class BBOMappingResult {

    private List<Role> roles;
    private List<Process> processes;
    private List<FlowElement> flowElements;

    public BBOMappingResult() {
        roles = new ArrayList<>();
        processes = new ArrayList<>();
        flowElements = new ArrayList<>();
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }

    public List<FlowElement> getFlowElements() {
        return flowElements;
    }

    public void setFlowElements(List<FlowElement> flowElements) {
        this.flowElements = flowElements;
    }
}
