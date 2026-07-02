import 'package:flutter/material.dart';
import 'config/theme.dart';
import 'router/app_router.dart';

class NgFieldsApp extends StatelessWidget {
  const NgFieldsApp({super.key});

  @override
  Widget build(BuildContext context) {
    final router = AppRouter();
    return MaterialApp.router(
      title: 'NG-Fields',
      theme: AppTheme.light,
      routerConfig: router.config(),
      debugShowCheckedModeBanner: false,
    );
  }
}
