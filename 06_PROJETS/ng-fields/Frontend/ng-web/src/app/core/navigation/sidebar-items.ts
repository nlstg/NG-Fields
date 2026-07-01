export interface NavSubItem {
  id: string;
  title: string;
  url: string;
  icon?: string;
  badge?: 'new' | 'soon';
  disabled?: boolean;
  newTab?: boolean;
}

export interface NavMainItem {
  id: string;
  title: string;
  url?: string;
  icon?: string;
  badge?: 'new' | 'soon';
  disabled?: boolean;
  newTab?: boolean;
  subItems?: NavSubItem[];
}

export interface NavGroup {
  id: number;
  label?: string;
  items: NavMainItem[];
}

export const sidebarItems: NavGroup[] = [
  {
    id: 1,
    label: 'Dashboards',
    items: [
      { id: 'default-v1', title: 'Default V1', url: '/dashboard/default-v1', icon: 'layout-dashboard' },
    ],
  },
  {
    id: 2,
    label: 'Pages',
    items: [
      {
        id: 'authentication', title: 'Authentication', icon: 'fingerprint',
        subItems: [
          { id: 'auth-login', title: 'Login v1', url: '/login', newTab: true },
          { id: 'auth-register', title: 'Register v1', url: '/register', newTab: true },
        ],
      },
    ],
  },
];

export const users = [
  {
    id: '1',
    name: 'Admin NG-Fields',
    email: 'admin@ng-fields.ngs.tg',
    avatar: '',
    role: 'administrator',
  },
];
