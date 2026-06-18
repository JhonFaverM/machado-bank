import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AccountService } from '../../../core/services/account.service';
import { Account as AccountModel } from '../../../models/account.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './account.html',
  styleUrls: ['./account.css'],
})
export class Account implements OnInit {

  accounts: AccountModel[] = [];
  loading = false;
  showBalance = true;

  constructor(
    private accountService: AccountService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAccounts();
  }

  // Cargar cuenta
  loadAccounts() {
    this.loading = true;

    this.accountService.getMyAccounts().subscribe({
      next: (res) => {
        this.accounts = [...res];
        this.loading = false;

         // 3. LA MAGIA: Fuerza a Angular a detectar el cambio
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
        alert('Error al cargar cuentas');
      }
    });
  }

  toggleBalance() {
    this.showBalance = !this.showBalance;
  }

  // Función para mostrar solo los últimos 5 dígitos
  formatAccountNumber(accountNumber: string): string {
    if (!accountNumber) return '';
    const last5 = accountNumber.slice(-5);
    return `****${last5}`;
  }

  // Crear cuenta
  createAccount() {
    this.accountService.createAccount().subscribe({
      next: () => {
        alert('Cuenta creada correctamente');
        this.loadAccounts(); // refrescar lista
      },
      error: (err) => {
        console.error(err);
        alert('Error al crear cuenta');
      }
    });
  }
}