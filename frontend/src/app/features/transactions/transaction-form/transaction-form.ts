import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { TransactionService } from '../../../core/services/transaction.service';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { AccountService } from '../../../core/services/account.service';

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transaction-form.html',
  styleUrls: ['./transaction-form.css']
})
export class TransactionForm implements OnInit {
  transactionForm: FormGroup;
  operationType: string = '';
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private accountService: AccountService,
    private router: Router
  ) {
    this.transactionForm = this.fb.group({
      accountNumber: ['', [Validators.required]],
      amount: ['', [Validators.required, Validators.min(1000)]],
      toAccountNumber: ['']
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.operationType = params['type']?.toUpperCase(); // Aseguramos mayúsculas para el switch
      this.setupForm();
      this.loadMyAccount();
    });
  }

  // 3. Método para pre-cargar el número de cuenta
 loadMyAccount() {
    this.accountService.getMyAccounts().subscribe({
      next: (accounts) => {
        // Verificamos que el arreglo no esté vacío
        if (accounts && accounts.length > 0) {
          // Tomamos la primera cuenta (única) y su número
          this.transactionForm.patchValue({
            accountNumber: accounts[0].accountNumber
          });
        }
      },
      error: (err) => {
        console.error('Error cargando cuenta origen', err);
      }
    });
  }

  setupForm() {
    const toAccountControl = this.transactionForm.get('toAccountNumber');
    if (this.operationType === 'TRANSFER') {
      toAccountControl?.setValidators([Validators.required]);
    } else {
      toAccountControl?.clearValidators();
    }
    toAccountControl?.updateValueAndValidity();
  }

  getLabelMonto(): string {
    if (this.operationType === 'DEPOSIT') return 'consignar';
    if (this.operationType === 'TRANSFER') return 'transferir';
    return 'retirar';
  }

  onSubmit() {
    if (this.transactionForm.invalid) return;

    this.loading = true;
    const data = this.transactionForm.value;
    let request$: Observable<any>;

    // Selección de operación con switch
    switch (this.operationType) {
      case 'TRANSFER':
        request$ = this.transactionService.transfer({
          fromAccountNumber: data.accountNumber,
          toAccountNumber: data.toAccountNumber,
          amount: data.amount
        });
        break;
      case 'DEPOSIT':
        request$ = this.transactionService.deposit(data);
        break;
      case 'WITHDRAW':
        request$ = this.transactionService.withdraw(data);
        break;
      default:
        console.error('Tipo de operación no soportado');
        this.loading = false;
        return;
    }

    request$.subscribe({
      next: () => {
        alert('Operación realizada con éxito');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        alert('Error: ' + (err.error?.message || 'No se pudo completar la operación'));
      }
    });
  }
}
