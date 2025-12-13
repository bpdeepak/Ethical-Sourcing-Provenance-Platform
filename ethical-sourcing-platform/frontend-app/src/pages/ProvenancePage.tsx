import { useState } from 'react';
import { provenanceService } from '../services/api';
import { motion } from 'framer-motion';
import { Search, MapPin, Clock } from 'lucide-react';

export default function ProvenancePage() {
  const [activeTab, setActiveTab] = useState<'record' | 'track'>('record');
  const [formData, setFormData] = useState({
    supplierId: '',
    assetId: '',
    parentAssetId: '',
    location: ''
  });
  // Tracking State
  const [searchId, setSearchId] = useState('');
  const [history, setHistory] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const { supplierId, ...eventData } = formData;
      await provenanceService.recordEvent(supplierId, eventData);
      alert('Event Recorded Successfully');
    } catch (err) {
      alert('Failed to record event. Check Supplier Trust Score.');
    }
  };

  const handleTrack = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await provenanceService.getHistory(searchId);
      setHistory(res.data);
    } catch (err) {
      alert('Failed to fetch history');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-8">
      <div className="flex justify-center space-x-4 mb-8">
        <button
          onClick={() => setActiveTab('record')}
          className={`px-6 py-2 rounded-full font-medium transition ${
            activeTab === 'record' ? 'bg-primary-600 text-white shadow-lg' : 'bg-white text-slate-600 hover:bg-slate-50'
          }`}
        >
          Record Event
        </button>
        <button
          onClick={() => setActiveTab('track')}
          className={`px-6 py-2 rounded-full font-medium transition ${
            activeTab === 'track' ? 'bg-primary-600 text-white shadow-lg' : 'bg-white text-slate-600 hover:bg-slate-50'
          }`}
        >
          Track Asset
        </button>
      </div>

      {activeTab === 'record' ? (
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="glass-card p-8 max-w-2xl mx-auto"
        >
          <h2 className="text-2xl font-bold text-slate-900 mb-6">Record Provenance Event</h2>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-slate-700">Supplier ID (Who is shipping?)</label>
              <input 
                type="text" 
                value={formData.supplierId}
                onChange={e => setFormData({...formData, supplierId: e.target.value})}
                className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 p-2 border"
                placeholder="e.g. SUP-001"
                required
              />
            </div>
            
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-slate-700">Asset ID</label>
                <input 
                  type="text" 
                  value={formData.assetId}
                  onChange={e => setFormData({...formData, assetId: e.target.value})}
                  className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 p-2 border"
                  placeholder="e.g. ITEM-123"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700">Parent Asset ID</label>
                <input 
                  type="text" 
                  value={formData.parentAssetId}
                  onChange={e => setFormData({...formData, parentAssetId: e.target.value})}
                  className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 p-2 border"
                  placeholder="Optional"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700">Current Location</label>
              <input 
                type="text" 
                value={formData.location}
                onChange={e => setFormData({...formData, location: e.target.value})}
                className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 p-2 border"
                required
              />
            </div>

            <button type="submit" className="w-full bg-primary-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-primary-700 transition shadow-lg shadow-primary-500/30">
              Record Event on Ledger
            </button>
          </form>
        </motion.div>
      ) : (
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-8"
        >
          <div className="glass-card p-8 max-w-2xl mx-auto">
            <h2 className="text-2xl font-bold text-slate-900 mb-6">Track Asset History</h2>
            <form onSubmit={handleTrack} className="flex gap-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-3 h-5 w-5 text-slate-400" />
                <input 
                  type="text" 
                  value={searchId}
                  onChange={e => setSearchId(e.target.value)}
                  className="pl-10 block w-full rounded-lg border-slate-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 p-2 border"
                  placeholder="Enter Asset ID (e.g. ASSET-100)"
                  required
                />
              </div>
              <button type="submit" className="bg-primary-600 text-white py-2 px-6 rounded-lg font-medium hover:bg-primary-700 transition">
                Track
              </button>
            </form>
          </div>

          {/* Timeline */}
          <div className="max-w-3xl mx-auto">
            {history.map((event, index) => (
              <motion.div 
                key={event.id}
                initial={{ opacity: 0, x: -20 }}
                animate={{ opacity: 1, x: 0 }}
                transition={{ delay: index * 0.1 }}
                className="relative pl-8 pb-8 border-l-2 border-primary-200 last:border-0"
              >
                <div className="absolute -left-[9px] top-0 h-4 w-4 rounded-full bg-primary-600 ring-4 ring-white" />
                <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="text-lg font-semibold text-slate-900">Asset: {event.assetId}</h3>
                    <span className="text-xs font-mono bg-slate-100 px-2 py-1 rounded text-slate-600">ID: {event.id}</span>
                  </div>
                  <div className="grid grid-cols-2 gap-4 text-sm text-slate-600">
                    <div className="flex items-center gap-2">
                      <MapPin className="h-4 w-4 text-primary-500" />
                      {event.location}
                    </div>
                    <div className="flex items-center gap-2">
                      <Clock className="h-4 w-4 text-primary-500" />
                      {new Date(event.timestamp).toLocaleString()}
                    </div>
                  </div>
                  {event.parentAssetId && (
                    <div className="mt-3 pt-3 border-t border-slate-100 text-sm">
                      <span className="text-slate-500">Derived from: </span>
                      <span className="font-medium text-slate-900">{event.parentAssetId}</span>
                    </div>
                  )}
                </div>
              </motion.div>
            ))}
            {history.length === 0 && !loading && searchId && (
              <div className="text-center text-slate-500 py-8">No history found for this asset.</div>
            )}
          </div>
        </motion.div>
      )}
    </div>
  );
}
