package org.example.mapper.org2bbo;

import org.example.mapper.AbstractMappingResult;
import org.example.model.bbo.model.Thing;
import org.example.service.OrganizationAsBbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Org2BboMappingResult extends AbstractMappingResult<Thing> {

    private OrganizationAsBbo organizationAsBbo;
    private List<Warning> warnings;

    public Org2BboMappingResult(Map<String, Thing> mappedObjectsById) {
        super(mappedObjectsById);
        organizationAsBbo = new OrganizationAsBbo();
        warnings = new ArrayList<>();
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Warning> warnings) {
        this.warnings = warnings;
    }

    public OrganizationAsBbo getOrganizationBbo() {
        return organizationAsBbo;
    }

    public void setOrganizationBbo(OrganizationAsBbo organizationAsBbo) {
        this.organizationAsBbo = organizationAsBbo;
    }
}
