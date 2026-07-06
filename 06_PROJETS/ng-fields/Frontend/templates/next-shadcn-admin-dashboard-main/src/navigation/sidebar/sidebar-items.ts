import {
  Banknote,
  Calendar,
  ChartBar,
  CheckSquare,
  Download,
  Forklift,
  Gauge,
  GraduationCap,
  Kanban,
  LayoutDashboard,
  ListTodo,
  Lock,
  type LucideIcon,
  Mail,
  MessageSquare,
  ReceiptText,
  Server,
  Settings,
  ShoppingBag,
  SquareArrowUpRight,
  UserCog,
  Users,
  Wrench,
} from "lucide-react";

export type NavBadge = "new" | "soon";

export interface NavSubItem {
  id: string;
  title: string;
  url: string;
  icon?: LucideIcon;
  badge?: NavBadge;
  disabled?: boolean;
  newTab?: boolean;
}

interface NavItemBase {
  id: string;
  title: string;
  icon?: LucideIcon;
  badge?: NavBadge;
  disabled?: boolean;
  newTab?: boolean;
}

export interface NavMainLinkItem extends NavItemBase {
  url: string;
  subItems?: never;
}

export interface NavMainParentItem extends NavItemBase {
  subItems: NavSubItem[];
}

export type NavMainItem = NavMainLinkItem | NavMainParentItem;

export interface NavGroup {
  id: number;
  label?: string;
  items: NavMainItem[];
}

export const sidebarItems: NavGroup[] = [
  {
    id: 1,
    label: "Dashboard",
    items: [
      {
        id: "overview",
        title: "Overview",
        url: "/dashboard/overview",
        icon: LayoutDashboard,
      },
      {
        id: "crm",
        title: "CRM",
        url: "/dashboard/crm",
        icon: ChartBar,
      },
      {
        id: "finance",
        title: "Finance",
        url: "/dashboard/finance",
        icon: Banknote,
      },
      {
        id: "analytics",
        title: "Analytics",
        url: "/dashboard/analytics",
        icon: Gauge,
      },
      {
        id: "productivity",
        title: "Productivity",
        url: "/dashboard/productivity",
        icon: ListTodo,
      },
      {
        id: "ecommerce",
        title: "E-commerce",
        url: "/dashboard/ecommerce",
        icon: ShoppingBag,
      },
      {
        id: "academy",
        title: "Academy",
        url: "/dashboard/academy",
        icon: GraduationCap,
      },
      {
        id: "logistics",
        title: "Logistics",
        url: "/dashboard/logistics",
        icon: Forklift,
      },
      {
        id: "infrastructure",
        title: "Infrastructure",
        url: "/dashboard/infrastructure",
        icon: Server,
        badge: "new",
      },
    ],
  },
  {
    id: 2,
    label: "Field Operations",
    items: [
      {
        id: "interventions",
        title: "Interventions",
        url: "#",
        icon: Wrench,
        badge: "soon",
        disabled: true,
      },
      {
        id: "clients",
        title: "Clients",
        url: "#",
        icon: Users,
        badge: "soon",
        disabled: true,
      },
      {
        id: "technicians",
        title: "Technicians",
        url: "#",
        icon: UserCog,
        badge: "soon",
        disabled: true,
      },
    ],
  },
  {
    id: 3,
    label: "Reports",
    items: [
      {
        id: "analytics-reports",
        title: "Analytics",
        url: "#",
        icon: ChartBar,
        badge: "soon",
        disabled: true,
      },
      {
        id: "exports",
        title: "Exports",
        url: "#",
        icon: Download,
        badge: "soon",
        disabled: true,
      },
    ],
  },
  {
    id: 4,
    label: "Administration",
    items: [
      {
        id: "users",
        title: "Users",
        url: "/dashboard/users",
        icon: Users,
      },
      {
        id: "roles",
        title: "Roles",
        url: "/dashboard/roles",
        icon: Lock,
      },
      {
        id: "settings",
        title: "Settings",
        url: "#",
        icon: Settings,
        badge: "soon",
        disabled: true,
      },
    ],
  },
  {
    id: 5,
    label: "Pages",
    items: [
      {
        id: "email",
        title: "Email",
        url: "/dashboard/mail",
        icon: Mail,
        disabled: true,
      },
      {
        id: "chat",
        title: "Chat",
        url: "/dashboard/chat",
        icon: MessageSquare,
        disabled: true,
      },
      {
        id: "calendar",
        title: "Calendar",
        url: "/dashboard/calendar",
        icon: Calendar,
        disabled: true,
      },
      {
        id: "kanban",
        title: "Kanban",
        url: "/dashboard/kanban",
        icon: Kanban,
        disabled: true,
      },
      {
        id: "tasks",
        title: "Tasks",
        url: "/dashboard/tasks",
        icon: CheckSquare,
        disabled: true,
      },
      {
        id: "invoice",
        title: "Invoice",
        url: "/dashboard/invoice",
        icon: ReceiptText,
        disabled: true,
      },
      {
        id: "pages-users",
        title: "Users",
        url: "/dashboard/users",
        icon: Users,
        disabled: true,
      },
      {
        id: "pages-roles",
        title: "Roles",
        url: "/dashboard/roles",
        icon: Lock,
        disabled: true,
      },
    ],
  },
  {
    id: 6,
    label: "Legacy",
    items: [
      {
        id: "legacy-dashboards",
        title: "Dashboards",
        subItems: [
          { id: "legacy-default", title: "Default V1", url: "/dashboard/default-v1" },
        ],
      },
    ],
  },
  {
    id: 7,
    label: "Misc",
    items: [
      {
        id: "others",
        title: "Others",
        url: "/dashboard/coming-soon",
        icon: SquareArrowUpRight,
        badge: "soon",
        disabled: true,
      },
    ],
  },
];
