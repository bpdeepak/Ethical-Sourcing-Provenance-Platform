import { useState, useEffect } from 'react';
import { supplierService } from '../services/api';
import { motion } from 'framer-motion';

export default function SupplierPage() {
  const [suppliers, setSuppliers] = useState<any[]>([]);
  const [formData, setFormData] = useState({
    supplierId: '',
    name: '',
    location: '',
    trustScore: 100,
    status: 'ACTIVE'
  });

  useEffect(() => {
    loadSuppliers();
  }, []);

  const loadSuppliers = async () => {
    try {
      const res = await supplierService.getAll();
      setSuppliers(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await supplierService.register(formData);
      loadSuppliers();
      setFormData({ supplierId: '', name: '', location: '', trustScore: 100, status: 'ACTIVE' });
    } catch (err) {
      alert('Failed to register supplier');
    }
  };

  return (
    <div className="space-y-8">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-slate-900">Supplier Management</h2>
      </div>

      <div className="grid md:grid-cols-2 gap-8">
        {/* Registration Form */}
        <motion.div 
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          className="glass-card p-6"
        >
          <h3 className="text-lg font-semibold mb-4">Register New Supplier</h3>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-slate-700">Supplier ID</label>
              <input 
                type="text" 
                value={formData.supplierId}
                onChange={e => setFormData({...formData, supplierId: e.target.value})}
                className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm p-2 border"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700">Name</label>
              <input 
                type="text" 
                value={formData.name}
                onChange={e => setFormData({...formData, name: e.target.value})}
                className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm p-2 border"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700">Location</label>
              <input 
                type="text" 
                value={formData.location}
                onChange={e => setFormData({...formData, location: e.target.value})}
                className="mt-1 block w-full rounded-md border-slate-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm p-2 border"
                required
              />
            </div>
            <button type="submit" className="w-full bg-primary-600 text-white py-2 px-4 rounded-md hover:bg-primary-700 transition">
              Register Supplier
            </button>
          </form>
        </motion.div>

        {/* Supplier List */}
        <div className="space-y-4">
          <h3 className="text-lg font-semibold">Registered Suppliers</h3>
          {suppliers.map((s) => (
            <motion.div 
              key={s.id}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="bg-white p-4 rounded-lg shadow border border-slate-100 flex justify-between items-center"
            >
              <div>
                <p className="font-medium text-slate-900">{s.name}</p>
                <p className="text-sm text-slate-500">{s.supplierId} • {s.location}</p>
              </div>
              <div className={`px-3 py-1 rounded-full text-sm font-medium ${s.trustScore > 80 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                Score: {s.trustScore}
              </div>
            </motion.div>
          ))}
        </div>
      </div>
    </div>
  );
}
