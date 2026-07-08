export interface Intervention {
  id: number;
  reference: string;
  client: {
    id: number;
    name: string;
    contact: string;
    phone: string;
    email: string;
    address: string;
  };
  type: 'Maintenance' | 'Dépannage' | 'Installation' | 'Mise à jour' | 'Audit' | 'Autre';
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  technician: {
    id: number;
    name: string;
    avatar: string;
    phone: string;
  };
  createdAt: string;
  scheduledAt: string;
  startedAt?: string;
  completedAt?: string;
  estimatedDuration: number;
  actualDuration?: number;
  description: string;
  notes: string;
  result?: 'RESOLVED' | 'PARTIAL' | 'UNRESOLVED';
  attachments: {
    id: number;
    filename: string;
    size: number;
    uploadedAt: string;
    uploadedBy: string;
  }[];
  timeline: TimelineEntry[];
}

export interface TimelineEntry {
  id: number;
  type: 'status_change' | 'note_added' | 'assigned' | 'file_added' | 'duration_updated';
  timestamp: string;
  actor: string;
  detail: string;
  metadata?: Record<string, any>;
}

export interface Note {
  id?: number;
  text: string;
  author: string;
  timestamp: string;
  isInternal: boolean;
}
