#!/bin/bash

BASE_URL="http://localhost:8080"

echo "Waiting for services..."
sleep 30

# 1. Register Supplier
echo "1. Registering Supplier..."
curl -X POST "$BASE_URL/api/suppliers" \
  -H "Content-Type: application/json" \
  -d '{
    "supplierId": "SUP-AUDIT-1",
    "name": "Audit Test Supplier",
    "location": "Berlin",
    "trustScore": 95,
    "status": "ACTIVE"
  }'
echo -e "\n"

# 2. Record Audit (Valid)
echo "2. Recording Audit (Valid)..."
curl -X POST "$BASE_URL/api/audits" \
  -H "Content-Type: application/json" \
  -d '{
    "supplierId": "SUP-AUDIT-1",
    "complianceStatus": "COMPLIANT",
    "comments": "Passed ISO check"
  }'
echo -e "\n"

# 3. List Audits
echo "3. Listing Audits..."
curl "$BASE_URL/api/audits/supplier/SUP-AUDIT-1"
echo -e "\n"

# 4. Record Audit (Invalid - Supplier Not Found)
echo "4. Recording Audit (Invalid)..."
curl -X POST "$BASE_URL/api/audits" \
  -H "Content-Type: application/json" \
  -d '{
    "supplierId": "SUP-INVALID",
    "complianceStatus": "COMPLIANT",
    "comments": "Should fail"
  }'
echo -e "\n"
echo "Done."
