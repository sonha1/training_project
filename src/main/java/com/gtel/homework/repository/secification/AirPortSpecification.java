package com.gtel.homework.repository.secification;

import com.gtel.homework.entity.AirportEntity;
import com.gtel.homework.entity.User;
import com.gtel.homework.utils.StringUtils;
import com.gtel.homework.utils.TextUtils;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AirPortSpecification {
    private final String FIELD_NAME = "name";
    private final List<Specification<User>> specifications = new ArrayList<>();

    public static AirPortSpecification builder() {
        return new AirPortSpecification();
    }

    public void withNamLike(final String str) {
        if (TextUtils.isBlank(str)) {
            return;
        }

        List<String> q = TextUtils.handleSearchText(str);
        specifications.add(
                (root, query, builder) -> builder.or(
                        q.stream()
                                .map(s -> {
                                    Expression<String> name;
                                    name = builder.function("remove_accents", String.class, root.get(FIELD_NAME));
                                    name = builder.function("strip_html_tags", String.class, name);
                                    name = builder.upper(name);
                                    return builder.like(name, StringUtils.getStringLike(str));
                                }).toArray(Predicate[]::new)
                )
        );
    }

    public Specification<User> build() {
        return (root, query, builder) -> builder.and(
                specifications.stream()
                        .filter(Objects::nonNull)
                        .map(spec -> spec.toPredicate(root, query, builder))
                        .toArray(Predicate[]::new)
        );
    }

    public static Specification<AirportEntity> getSpec(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals("name") && !TextUtils.isBlank(value)) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("name"), value));
                }

                if (key.equals("airportgroupcode") && !TextUtils.isBlank(value)) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("airportgroupcode"), value));
                }

                if (key.equals("language") && !TextUtils.isBlank(value)) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("language"), value));
                }

                if (key.equals("priority") && !TextUtils.isBlank(value)) {

                    try {
                        Integer intValue = Integer.valueOf(value);
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("priority"), intValue));
                    } catch (Exception e) {

                    }

                }
            }

            return predicate;
        };
    }
}
