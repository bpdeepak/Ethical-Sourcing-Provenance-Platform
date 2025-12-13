import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import { supplierService } from '../services/api';
import { MapContainer, TileLayer, CircleMarker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

export default function Dashboard() {
  const [stats, setStats] = useState({
    totalSuppliers: 0,
    avgTrustScore: 0,
    activeSuppliers: 0
  });
  const [suppliers, setSuppliers] = useState<any[]>([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const res = await supplierService.getAll();
      const data = res.data;
      setSuppliers(data);
      
      const total = data.length;
      const active = data.filter((s: any) => s.status === 'ACTIVE').length;
      const avgScore = total > 0 
        ? Math.round(data.reduce((acc: number, s: any) => acc + s.trustScore, 0) / total) 
        : 0;

      setStats({
        totalSuppliers: total,
        activeSuppliers: active,
        avgTrustScore: avgScore
      });
    } catch (err) {
      console.error('Failed to load dashboard data', err);
    }
  };

  const getRiskColor = (score: number) => {
    if (score >= 80) return '#22c55e'; // Green
    if (score >= 50) return '#eab308'; // Yellow
    return '#ef4444'; // Red
  };

  // Mock coordinates for demo (since backend doesn't have lat/lng yet)
  // In a real app, we would geocode the 'location' string
  const getCoordinates = (_location: string, index: number) => {
    // Simple deterministic offset to spread points on map
    const baseLat = 20.5937;
    const baseLng = 78.9629;
    return [baseLat + (index * 5), baseLng + (index * 5)];
  };

  return (
    <div className="space-y-8">
      <motion.div 
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="text-center space-y-4"
      >
        <h1 className="text-4xl font-bold text-slate-900">
          Ethical Sourcing <span className="text-primary-600">Command Center</span>
        </h1>
        <p className="text-lg text-slate-600 max-w-2xl mx-auto">
          Real-time visibility into your supply chain risk and performance.
        </p>
      </motion.div>

      {/* Analytics Cards */}
      <div className="grid md:grid-cols-3 gap-6">
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="glass-card p-6 space-y-2"
        >
          <p className="text-slate-500 font-medium">Total Suppliers</p>
          <h3 className="text-4xl font-bold text-slate-900">{stats.totalSuppliers}</h3>
          <div className="text-sm text-green-600 font-medium">
            {stats.activeSuppliers} Active
          </div>
        </motion.div>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="glass-card p-6 space-y-2"
        >
          <p className="text-slate-500 font-medium">Avg. Trust Score</p>
          <h3 className="text-4xl font-bold text-slate-900">{stats.avgTrustScore}</h3>
          <div className="w-full bg-slate-200 rounded-full h-2.5 mt-2">
            <div 
              className="bg-primary-600 h-2.5 rounded-full" 
              style={{ width: `${stats.avgTrustScore}%` }}
            ></div>
          </div>
        </motion.div>

        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
          className="glass-card p-6 space-y-2"
        >
          <p className="text-slate-500 font-medium">System Status</p>
          <h3 className="text-4xl font-bold text-green-600">Healthy</h3>
          <p className="text-sm text-slate-500">All services operational</p>
        </motion.div>
      </div>

      {/* Risk Heatmap */}
      <motion.div 
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ delay: 0.4 }}
        className="glass-card p-6"
      >
        <h3 className="text-xl font-bold text-slate-900 mb-4">Supply Chain Risk Heatmap</h3>
        <div className="h-[400px] rounded-xl overflow-hidden border border-slate-200">
          <MapContainer center={[20, 0]} zoom={2} style={{ height: '100%', width: '100%' }}>
            <TileLayer
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {suppliers.map((s, index) => (
              <CircleMarker 
                key={s.id}
                center={getCoordinates(s.location, index) as [number, number]}
                pathOptions={{ color: getRiskColor(s.trustScore), fillColor: getRiskColor(s.trustScore), fillOpacity: 0.7 }}
                radius={10}
              >
                <Popup>
                  <div className="p-2">
                    <h4 className="font-bold">{s.name}</h4>
                    <p className="text-sm text-slate-600">{s.location}</p>
                    <div className="mt-2 text-xs font-bold" style={{ color: getRiskColor(s.trustScore) }}>
                      Trust Score: {s.trustScore}
                    </div>
                  </div>
                </Popup>
              </CircleMarker>
            ))}
          </MapContainer>
        </div>
      </motion.div>
    </div>
  );
}
