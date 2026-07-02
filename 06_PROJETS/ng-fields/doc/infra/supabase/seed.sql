-- Seed data for NG-Fields development
-- Run via Supabase SQL Editor or supabase db push

INSERT INTO public.users (id, email, password_hash, name, role, department)
VALUES
  ('00000000-0000-0000-0000-000000000001', 'admin@ngfields.com',
   '$2a$12$LJ3m4ys3Lk0TSwOXlbgKueMKImd1SJ3Z0xZ0xZ0xZ0xZ0xZ0xZ0xZ',
   'Admin NG-Fields', 'ADMIN', 'Direction'),
  ('00000000-0000-0000-0000-000000000002', 'tech1@ngfields.com',
   '$2a$12$LJ3m4ys3Lk0TSwOXlbgKueMKImd1SJ3Z0xZ0xZ0xZ0xZ0xZ0xZ0xZ',
   'Koffi A.', 'TECHNICIAN', 'Lomé'),
  ('00000000-0000-0000-0000-000000000003', 'tech2@ngfields.com',
   '$2a$12$LJ3m4ys3Lk0TSwOXlbgKueMKImd1SJ3Z0xZ0xZ0xZ0xZ0xZ0xZ0xZ',
   'Ama D.', 'TECHNICIAN', 'Kara');

-- Password for all: password123
