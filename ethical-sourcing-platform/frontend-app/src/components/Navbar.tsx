import { Link } from 'react-router-dom';
import { ShieldCheck } from 'lucide-react';

export default function Navbar() {
  return (
    <nav className="bg-white border-b border-slate-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Link to="/" className="flex items-center gap-2">
              <ShieldCheck className="h-8 w-8 text-primary-600" />
              <span className="text-xl font-bold text-slate-900">EthicalSource</span>
            </Link>
          </div>
          <div className="flex items-center gap-6">
            <Link to="/" className="text-slate-600 hover:text-primary-600 font-medium">Dashboard</Link>
            <Link to="/suppliers" className="text-slate-600 hover:text-primary-600 font-medium">Suppliers</Link>
            <Link to="/provenance" className="text-slate-600 hover:text-primary-600 font-medium">Provenance</Link>
          </div>
        </div>
      </div>
    </nav>
  );
}
