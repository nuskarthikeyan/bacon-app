/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { BaconAppTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BaconDetailComponent } from '../../../../../../main/webapp/app/entities/bacon/bacon-detail.component';
import { BaconService } from '../../../../../../main/webapp/app/entities/bacon/bacon.service';
import { Bacon } from '../../../../../../main/webapp/app/entities/bacon/bacon.model';

describe('Component Tests', () => {

    describe('Bacon Management Detail Component', () => {
        let comp: BaconDetailComponent;
        let fixture: ComponentFixture<BaconDetailComponent>;
        let service: BaconService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BaconAppTestModule],
                declarations: [BaconDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BaconService,
                    JhiEventManager
                ]
            }).overrideTemplate(BaconDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BaconDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BaconService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Bacon(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.bacon).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
