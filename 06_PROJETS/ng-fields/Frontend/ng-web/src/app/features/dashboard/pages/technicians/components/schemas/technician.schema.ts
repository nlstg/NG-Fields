export interface Technician {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  status: 'AVAILABLE' | 'BUSY' | 'ON_LEAVE' | 'INACTIVE';
  avatar: string;
  hireDate: string;
  skills: TechnicianSkill[];
  interventions: {
    thisMonth: number;
    total: number;
    avgDuration: number;
  };
  rating: {
    average: number;
    count: number;
  };
}

export interface TechnicianSkill {
  name: string;
  level: 'EXPERT' | 'ADVANCED' | 'INTERMEDIATE' | 'BEGINNER';
}

export interface TechnicianDetailView extends Technician {
  address: string;
  city: string;
  postalCode: string;
  country: string;
  department: string;
  manager: string;
  managerEmail: string;
  certifications: Certification[];
  availability: Availability;
  interventionsThisMonth: InterventionRecord[];
  interventionsHistory: InterventionRecord[];
  ratings: ClientRating[];
  notes: string;
}

export interface Certification {
  name: string;
  issuer: string;
  issuedAt: string;
  expiresAt?: string;
}

export interface Availability {
  status: 'AVAILABLE' | 'BUSY' | 'ON_LEAVE';
  currentTask?: string;
  backUntil?: string;
  workingHours: {
    monday: string;
    tuesday: string;
    wednesday: string;
    thursday: string;
    friday: string;
    saturday: string;
    sunday: string;
  };
}

export interface InterventionRecord {
  id: number;
  reference: string;
  client: string;
  type: string;
  date: string;
  duration: number;
  status: string;
  rating?: number;
}

export interface ClientRating {
  id: number;
  clientName: string;
  rating: number;
  comment: string;
  date: string;
}
