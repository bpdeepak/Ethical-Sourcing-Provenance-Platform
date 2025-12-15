import axios from 'axios';

// Gateway URL
const API_URL = 'http://localhost:8080';

export const api = axios.create({
  baseURL: API_URL,
});

export const supplierService = {
  register: (data: any) => api.post('/supplier-service/api/suppliers', data),
  getAll: () => api.get('/supplier-service/api/suppliers'),
  getTrustScore: (id: string) => api.get(`/supplier-service/api/suppliers/${id}/trust-score`),
};

export const provenanceService = {
  recordEvent: (supplierId: string, data: any) => 
    api.post(`/provenance-service/api/provenance/events?supplierId=${supplierId}`, data),
  getHistory: (assetId: string) => api.get(`/provenance-service/api/provenance/history/${assetId}`),
};

export const auditService = {
  recordAudit: (data: any) => api.post('/audit-service/api/audits', data),
  getAudits: (supplierId: string) => api.get(`/audit-service/api/audits/supplier/${supplierId}`),
};
