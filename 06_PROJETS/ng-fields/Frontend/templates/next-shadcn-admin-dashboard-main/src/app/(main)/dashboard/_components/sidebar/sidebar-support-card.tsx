import { Card, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

export function SidebarSupportCard() {
  return (
    <Card size="sm" className="overflow-hidden shadow-none group-data-[collapsible=icon]:hidden">
      <CardHeader className="min-w-0 px-4">
        <CardTitle className="truncate text-sm">Need help?</CardTitle>
        <CardDescription className="line-clamp-2">
          Contact your administrator or the NG-STARs support team.
        </CardDescription>
      </CardHeader>
    </Card>
  );
}
