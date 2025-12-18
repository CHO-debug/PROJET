package com.example.micro_absence.Dto;

import java.time.LocalDate;

public class AbsenceDto {

    private String id;
    private LocalDate date;
    private String employeId;

    private String typeId;
    private String typeName;      // Ex : Congé payé, Maladie, Formation
    private String justification; // JUSTIFIE | NON_JUSTIFIE

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getEmployeId() { return employeId; }
    public void setEmployeId(String employeId) { this.employeId = employeId; }

    public String getTypeId() { return typeId; }
    public void setTypeId(String typeId) { this.typeId = typeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
}
