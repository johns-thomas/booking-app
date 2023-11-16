package com.project.app.booking.models;


import com.project.app.booking.enums.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="property")
public class PropertyEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    String title;

    @Column
    String description;

    @Column
    Float price;

    @Column
    String location;

    @Column
    String area;

    @Column
    int bedrooms;

    @Column
    int bathrooms;

    @Column
    String img;

    @Column
    //@Enumerated(EnumType.STRING)
    Type type;

    @OneToOne(mappedBy = "property")
    private ListingEntity listing;

    @OneToMany
    private Set<BookingEntity> bookings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PropertyEntity that = (PropertyEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }




}
