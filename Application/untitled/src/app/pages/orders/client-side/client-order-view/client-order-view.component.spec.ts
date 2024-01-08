import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientOrderViewComponent } from './client-order-view.component';

describe('ClientOrderViewComponent', () => {
  let component: ClientOrderViewComponent;
  let fixture: ComponentFixture<ClientOrderViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientOrderViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ClientOrderViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
