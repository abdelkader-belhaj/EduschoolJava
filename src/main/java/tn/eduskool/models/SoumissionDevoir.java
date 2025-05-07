package tn.eduskool.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import tn.eduskool.entities.Devoir;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoumissionDevoir {
    private int id;
    private LocalDateTime dateSoumission;
    private String fichierSoumis;
    private Integer noteObtenue;
    private Devoir devoir;
}
