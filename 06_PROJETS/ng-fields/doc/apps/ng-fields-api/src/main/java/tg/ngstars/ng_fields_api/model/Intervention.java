package tg.ngstars.ng_fields_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "interventions", schema = "intervention")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "local_id", unique = true, length = 50)
    private String localId;

    // ─── SECTION 1 : Infos generales ────────────────────────────────────────
    @NotNull
    @Column(nullable = false)
    private Instant date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InterventionStatus status = InterventionStatus.PENDING;

    @Column(length = 50)
    private String type;

    @Column(name = "technician_id", length = 36)
    private String technicianId;

    @Column(name = "technician_name", length = 100)
    private String technicianName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    // ─── SECTION 2 : Horaires ───────────────────────────────────────────────
    @Column(name = "departure_time")
    private LocalTime departureTime;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    @Column(name = "intervention_start_time")
    private LocalTime interventionStartTime;

    @Column(name = "intervention_end_time")
    private LocalTime interventionEndTime;

    @Column(name = "return_time")
    private LocalTime returnTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    // ─── SECTION 3 : Diagnostic ─────────────────────────────────────────────
    @Column(name = "problem_desc", columnDefinition = "TEXT")
    private String problemDesc;

    @Column(name = "openproject_ticket_id", length = 50)
    private String openProjectTicketId;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    // ─── SECTION 4 : Travaux et equipement ──────────────────────────────────
    @Column(name = "work_done", columnDefinition = "TEXT")
    private String workDone;

    @Column(name = "equipment_type", length = 100)
    private String equipmentType;

    @Column(name = "equipment_brand", length = 100)
    private String equipmentBrand;

    @Column(name = "equipment_model", length = 100)
    private String equipmentModel;

    @Column(name = "equipment_serial", length = 100)
    private String equipmentSerial;

    @Column(name = "equipment_location", length = 200)
    private String equipmentLocation;

    // ─── SECTION 5 : Pieces ─────────────────────────────────────────────────
    @OneToMany(mappedBy = "intervention", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterventionItem> items = new ArrayList<>();

    // ─── SECTION 6 : Resultat ───────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private InterventionResult result;

    // ─── SECTION 7 : Recommandations ────────────────────────────────────────
    @Column(columnDefinition = "TEXT")
    private String recommendations;

    // ─── SECTION 8 : Facturation et signatures ──────────────────────────────
    @Column(nullable = false)
    private Boolean billable = true;

    @Column(name = "billing_notes", columnDefinition = "TEXT")
    private String billingNotes;

    @Column(name = "signature_client_url", columnDefinition = "TEXT")
    private String signatureClientUrl;

    @Column(name = "signature_technician_url", columnDefinition = "TEXT")
    private String signatureTechnicianUrl;

    @Column(name = "signature_manager_url", columnDefinition = "TEXT")
    private String signatureManagerUrl;

    // ─── Metadonnees ────────────────────────────────────────────────────────
    @Column(name = "pdf_url", columnDefinition = "TEXT")
    private String pdfUrl;

    @Column(name = "synced_at")
    private Instant syncedAt;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        recalculateDuration();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
        recalculateDuration();
    }

    public void recalculateDuration() {
        if (interventionStartTime != null && interventionEndTime != null
                && !interventionEndTime.isBefore(interventionStartTime)) {
            long minutes = Duration.between(interventionStartTime, interventionEndTime).toMinutes();
            this.durationMinutes = (int) minutes;
        }
    }

    public UUID getId() { return id; }
    public String getLocalId() { return localId; }
    public void setLocalId(String localId) { this.localId = localId; }
    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }
    public InterventionStatus getStatus() { return status; }
    public void setStatus(InterventionStatus status) { this.status = status; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTechnicianId() { return technicianId; }
    public void setTechnicianId(String technicianId) { this.technicianId = technicianId; }
    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { this.departureTime = departureTime; }
    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public LocalTime getInterventionStartTime() { return interventionStartTime; }
    public void setInterventionStartTime(LocalTime t) { this.interventionStartTime = t; }
    public LocalTime getInterventionEndTime() { return interventionEndTime; }
    public void setInterventionEndTime(LocalTime t) { this.interventionEndTime = t; }
    public LocalTime getReturnTime() { return returnTime; }
    public void setReturnTime(LocalTime returnTime) { this.returnTime = returnTime; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public String getProblemDesc() { return problemDesc; }
    public void setProblemDesc(String problemDesc) { this.problemDesc = problemDesc; }
    public String getOpenProjectTicketId() { return openProjectTicketId; }
    public void setOpenProjectTicketId(String id) { this.openProjectTicketId = id; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getWorkDone() { return workDone; }
    public void setWorkDone(String workDone) { this.workDone = workDone; }
    public String getEquipmentType() { return equipmentType; }
    public void setEquipmentType(String t) { this.equipmentType = t; }
    public String getEquipmentBrand() { return equipmentBrand; }
    public void setEquipmentBrand(String b) { this.equipmentBrand = b; }
    public String getEquipmentModel() { return equipmentModel; }
    public void setEquipmentModel(String m) { this.equipmentModel = m; }
    public String getEquipmentSerial() { return equipmentSerial; }
    public void setEquipmentSerial(String s) { this.equipmentSerial = s; }
    public String getEquipmentLocation() { return equipmentLocation; }
    public void setEquipmentLocation(String l) { this.equipmentLocation = l; }
    public List<InterventionItem> getItems() { return items; }
    public InterventionResult getResult() { return result; }
    public void setResult(InterventionResult result) { this.result = result; }
    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String r) { this.recommendations = r; }
    public Boolean getBillable() { return billable; }
    public void setBillable(Boolean billable) { this.billable = billable; }
    public String getBillingNotes() { return billingNotes; }
    public void setBillingNotes(String n) { this.billingNotes = n; }
    public String getSignatureClientUrl() { return signatureClientUrl; }
    public void setSignatureClientUrl(String url) { this.signatureClientUrl = url; }
    public String getSignatureTechnicianUrl() { return signatureTechnicianUrl; }
    public void setSignatureTechnicianUrl(String url) { this.signatureTechnicianUrl = url; }
    public String getSignatureManagerUrl() { return signatureManagerUrl; }
    public void setSignatureManagerUrl(String url) { this.signatureManagerUrl = url; }
    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
    public Instant getSyncedAt() { return syncedAt; }
    public void setSyncedAt(Instant syncedAt) { this.syncedAt = syncedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
