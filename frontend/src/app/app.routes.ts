import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { authGuard } from './core/guards/auth.guard';
import { Dashboard } from './features/dashboard/dashboard';
import { Account } from './features/accounts/account/account';
import { Register } from './features/auth/register/register';
import { TransactionHistory } from './features/transactions/transaction-history/transaction-history';
import { TransactionForm } from './features/transactions/transaction-form/transaction-form';


export const routes: Routes = [

    { path: 'register', component: Register },
    { path: 'login', component: Login },
    { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
    { path: 'account', component: Account, canActivate: [authGuard] },
    { path: 'history/:accountNumber', component: TransactionHistory, canActivate: [authGuard] },
    { path: 'transaction/:type', component: TransactionForm, canActivate: [authGuard] },

    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: '**', redirectTo: 'login' } // Comodín por si escriben cualquier cosa
    
];
