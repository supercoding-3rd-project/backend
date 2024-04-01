package com.github.devsns.domain.user.userRoleEntity;

import com.github.devsns.domain.role.roleEntity.RoleEntity;
import com.github.devsns.domain.user.userEntities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_roles")
public class userRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long userRoleId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;
}
