$ErrorActionPreference = "Stop"
$BaseUrl = "http://localhost:8080"

Write-Host "Waiting for services..."
Start-Sleep -Seconds 30

# 1. Register Supplier
Write-Host "1. Registering Supplier..."
$supplierBody = @{
    supplierId = "SUP-AUDIT-1"
    name = "Audit Test Supplier"
    location = "Berlin"
    trustScore = 95
    status = "ACTIVE"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/suppliers" -ContentType "application/json" -Body $supplierBody
Write-Host "`n"

# 2. Record Audit (Valid)
Write-Host "2. Recording Audit (Valid)..."
$auditBody = @{
    supplierId = "SUP-AUDIT-1"
    complianceStatus = "COMPLIANT"
    comments = "Passed ISO check"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/audits" -ContentType "application/json" -Body $auditBody
Write-Host "`n"

# 3. List Audits
Write-Host "3. Listing Audits..."
Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/audits/supplier/SUP-AUDIT-1"
Write-Host "`n"

# 4. Record Audit (Invalid - Supplier Not Found)
Write-Host "4. Recording Audit (Invalid)..."
$invalidBody = @{
    supplierId = "SUP-INVALID"
    complianceStatus = "COMPLIANT"
    comments = "Should fail"
} | ConvertTo-Json

try {
    Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/audits" -ContentType "application/json" -Body $invalidBody
} catch {
    Write-Host "Caught expected error: $($_.Exception.Message)"
}
Write-Host "`n"

Write-Host "Done."
