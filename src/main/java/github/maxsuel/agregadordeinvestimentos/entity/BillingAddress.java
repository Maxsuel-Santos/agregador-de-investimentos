package github.maxsuel.agregadordeinvestimentos.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "billing_addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillingAddress {
    
    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "number", nullable = false)
    private Integer number;

}
