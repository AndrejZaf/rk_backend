package com.rarekickz.rk_inventory_service.specification;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.enums.Gender;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@UtilityClass
public class SneakerSpecification {

    public static Specification<Sneaker> createSneakerSpecification(final List<Long> brandIds,
                                                                    final List<Gender> genders,
                                                                    final List<Double> sizes,
                                                                    final String name) {
        return (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (nonNull(sizes) && !sizes.isEmpty()) {
                final Predicate sizesPredicate = root.join("sneakerSizes")
                        .get("sneakerSizeId")
                        .get("size")
                        .as(Double.class)
                        .in(sizes);
                predicates.add(sizesPredicate);
            }

            if (nonNull(brandIds) && !brandIds.isEmpty()) {
                final Predicate brandPredicate = root.get("brand")
                        .get("id")
                        .as(Long.class)
                        .in(brandIds);
                predicates.add(brandPredicate);
            }

            if (nonNull(genders) && !genders.isEmpty()) {
                final Predicate genderPredicate = root.get("gender")
                        .as(String.class)
                        .in(genders.stream()
                                .map(Enum::toString)
                                .toList());
                predicates.add(genderPredicate);
            }

            if (isNotBlank(name)) {
                final Predicate namePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
