package org.example.service;

import org.example.model.bbo.model.FlowElement;
import org.example.model.bbo.model.Process;
import org.example.model.bbo.model.Role;
import org.example.model.bbo.model.Thing;

import java.util.HashMap;
import java.util.Map;

public class BpmnAsBbo {

    private Map<String, Thing> allObjects;
    private Map<String, Role> performers;
    private Map<String, Process> processes;
    private Map<String, FlowElement> flowElements;

    public BpmnAsBbo() {
        this.allObjects = new HashMap<>();
        this.performers = new HashMap<>();
        this.processes = new HashMap<>();
        this.flowElements = new HashMap<>();
    }

    public Map<String, Thing> getAllObjects() {
        return allObjects;
    }

    public void setAllObjects(Map<String, Thing> allObjects) {
        this.allObjects = allObjects;
    }

    public Map<String, Role> getPerformers() {
        return performers;
    }

    public void setPerformers(Map<String, Role> performers) {
        this.performers = performers;
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
