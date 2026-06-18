import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Account as AccountModel } from '../../models/account.model';
import { AccountService } from '../../core/services/account.service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';



@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  userName: string = 'Usuario';
  userInitials: string = 'U';
  showBalance = true;
  loading = false;
  accounts: AccountModel[] = [];

  constructor(
    private accountService: AccountService,
    private cdr: ChangeDetectorRef,
    private authService: AuthService
  ){}

  onLogout() {
    if (confirm('¿Estás seguro que deseas cerrar sesión?')) {
      this.authService.logout();
    }
  }

  ngOnInit(): void {
    // 1. Prioridad: Cargar el nombre de la sesión para el saludo inmediato
    const storedName = sessionStorage.getItem('userName');
    
    if (storedName) {
      this.userName = storedName;
      this.userInitials = this.getInitials(storedName);
    }

    // 2. Cargar los datos financieros del servidor
    this.loadAccount();
  }

  // Genera iniciales limpias. Ejemplo: "Monica Riscanevo" -> "MR"
  private getInitials(name: string): string {
    if (!name) return 'U';
    const parts = name.split(' ').filter(p => p.length > 0);
    return parts
      .map(n => n[0])
      .join('')
      .toUpperCase()
      .substring(0, 2);
  }

  loadAccount(){
    this.loading = true;

    this.accountService.getMyAccounts().subscribe({
      next: (res) => {
        this.accounts = [...res]

        // 💡 Opcional: Si el nombre en la cuenta es más completo, lo actualizamos
        if (this.accounts.length > 0 && this.accounts[0].client) {
          this.userName = this.accounts[0].client.fullName;
          this.userInitials = this.getInitials(this.userName);
        }

        this.loading = false;
        this.cdr.detectChanges(); //Asegura que se cargue el saldo de inmediato
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

}
