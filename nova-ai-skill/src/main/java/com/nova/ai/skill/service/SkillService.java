package com.nova.ai.skill.service;

import com.nova.ai.skill.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Skill service manages registration and execution of skills.
 * Uses JDK 25 sealed classes for type-safe skill definitions.
 */
@Service
public class SkillService {

    private final Map<String, Skill> registry = new ConcurrentHashMap<>();

    public SkillService() {
        // Register built-in skills
        registry.put("data-query", new DataQuerySkill("data-query", "Data Query", "SELECT * FROM {}", Map.of()));
        registry.put("calculation", new CalculationSkill("calculation", "Calculator", "expression", Map.of()));
        registry.put("notification", new NotificationSkill("notification", "Notifier", "email", "Template: {}"));
        registry.put("external-api", new ExternalApiSkill("external-api", "External API", "/api/external", Map.of()));
    }

    public SkillResult execute(String skillId, SkillContext context) {
        Skill skill = registry.get(skillId);
        if (skill == null) {
            return new ErrorResult("Skill not found: " + skillId);
        }
        return skill.execute(context);
    }

    public void register(Skill skill) {
        registry.put(skill.id(), skill);
    }

    public Skill getSkill(String skillId) {
        return registry.get(skillId);
    }

    public List<Skill> listSkills() {
        return List.copyOf(registry.values());
    }

    public List<Skill> getSkillsByType(SkillType type) {
        return registry.values().stream()
            .filter(s -> s.type() == type)
            .toList();
    }

    /**
     * JDK 25 Pattern Matching: exhaustive switch on sealed SkillResult.
     */
    public String formatResult(SkillResult result) {
        return switch (result) {
            case SuccessResult s -> "SUCCESS: " + s.data();
            case ErrorResult e -> "ERROR: " + e.errorMessage();
        };
    }
}
