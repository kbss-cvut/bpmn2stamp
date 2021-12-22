package mapper.org2bbo;

import mapper.AbstractMappingResult;
import model.bbo.model.Thing;
import service.OrganizationBbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Org2BboMappingResult extends AbstractMappingResult<Thing> {

    private OrganizationBbo organizationBbo;
    private List<Warning> warnings;

    public Org2BboMappingResult(Map<String, Thing> mappedObjectsById) {
        super(mappedObjectsById);
        organizationBbo = new OrganizationBbo();
        warnings = new ArrayList<>();
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Warning> warnings) {
        this.warnings = warnings;
    }

    public OrganizationBbo getOrganizationBbo() {
        return organizationBbo;
    }

    public void setOrganizationBbo(OrganizationBbo organizationBbo) {
        this.organizationBbo = organizationBbo;
    }
}
