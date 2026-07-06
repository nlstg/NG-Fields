package tg.ngstars.interv.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "intervention_photos")
public class InterventionPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intervention_id", nullable = false)
    private Intervention intervention;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PhotoType type;

    private Double latitude;
    private Double longitude;

    @Column(name = "taken_at")
    private Instant takenAt;

    @Column(name = "original_filename", length = 200)
    private String originalFilename;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Intervention getIntervention() { return intervention; }
    public void setIntervention(Intervention i) { this.intervention = i; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public PhotoType getType() { return type; }
    public void setType(PhotoType type) { this.type = type; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double lat) { this.latitude = lat; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double lon) { this.longitude = lon; }
    public Instant getTakenAt() { return takenAt; }
    public void setTakenAt(Instant t) { this.takenAt = t; }
    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String f) { this.originalFilename = f; }
    public Instant getCreatedAt() { return createdAt; }
}
