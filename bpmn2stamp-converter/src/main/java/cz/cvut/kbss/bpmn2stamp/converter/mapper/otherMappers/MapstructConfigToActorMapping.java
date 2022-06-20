package cz.cvut.kbss.bpmn2stamp.converter.mapper.otherMappers;

import cz.cvut.kbss.bpmn2stamp.converter.mapper.SmartMapstructMapper;
import cz.cvut.kbss.bpmn2stamp.converter.model.actor.ActorMappings;
import cz.cvut.kbss.bpmn2stamp.converter.model.actor.Roles;
import cz.cvut.kbss.bpmn2stamp.converter.model.actorConfig.Configuration;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Group;
import org.mapstruct.Mapper;

@Mapper
public abstract class MapstructConfigToActorMapping extends SmartMapstructMapper {

	public ActorMappings doMapping(Configuration source) {
		if (source == null)
			return null;
		return actorMappingsToActorMappings(source.getActorMappings());
	}

	abstract ActorMappings actorMappingsToActorMappings(Configuration.ActorMappings source);

	abstract Group groupToGroup(Configuration.ActorMappings.ActorMapping.Groups groups);

	abstract Roles roleToRole(Configuration.ActorMappings.ActorMapping.Roles roles);

	abstract Configuration.ActorMappings.ActorMapping.Memberships membershipsToMemberships(Configuration.ActorMappings.ActorMapping.Memberships memberships);

}
